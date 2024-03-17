package com.kyungsuksong.doomsday.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.google.android.gms.maps.model.LatLng
import com.kyungsuksong.doomsday.compose.fire.FireCountryDialogState
import com.kyungsuksong.doomsday.compose.fire.FireSourceDropdownItem
import com.kyungsuksong.doomsday.data.room.FireEntity
import com.kyungsuksong.doomsday.repo.FireRepository
import com.kyungsuksong.doomsday.util.FIRE_LAST_SELECTED_COUNTRY
import com.kyungsuksong.doomsday.util.FIRE_WORK_TAG
import com.kyungsuksong.doomsday.util.FireCountry
import com.kyungsuksong.doomsday.util.FireCountry.fireCountryMap
import com.kyungsuksong.doomsday.util.SHARED_PREFERENCE_NAME
import com.kyungsuksong.doomsday.util.WORK_DATA_COUNTRY_KEY
import com.kyungsuksong.doomsday.util.getCurrentTimeForFire
import com.kyungsuksong.doomsday.util.getYesterdayTimeForFire
import com.kyungsuksong.doomsday.viewmodel.FireSourceType.ALL
import com.kyungsuksong.doomsday.viewmodel.FireSourceType.VIIRS_NOAA20_NRT
import com.kyungsuksong.doomsday.viewmodel.FireSourceType.VIIRS_NOAA21_NRT
import com.kyungsuksong.doomsday.viewmodel.FireSourceType.VIIRS_SNPP_NRT
import com.kyungsuksong.doomsday.worker.FireWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

object FireSourceType {
    const val ALL = 0
    const val VIIRS_SNPP_NRT = 1
    const val VIIRS_NOAA20_NRT = 2
    const val VIIRS_NOAA21_NRT = 3
}

@HiltViewModel
class FireViewModel @Inject constructor(
    fireRepository: FireRepository,
    private val workManager: WorkManager,
    @ApplicationContext private val appContext: Context
): ViewModel() {

    // 1. work state for the worker
    val workState: SharedFlow<List<WorkInfo>> =
        workManager
            .getWorkInfosByTagFlow(FIRE_WORK_TAG)
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            )

    private val sharedPreference = appContext.getSharedPreferences(
        SHARED_PREFERENCE_NAME,
        Context.MODE_PRIVATE
    )

    // 2. country
    private val _fireCountry = MutableStateFlow<String>(
        fireCountryMap[sharedPreference.getString(FIRE_LAST_SELECTED_COUNTRY, "United States")] ?: "USA"
    )
    private val fireCountry: StateFlow<String>
        get() = _fireCountry.asStateFlow()

    // 3. country lat lng
    @OptIn(ExperimentalCoroutinesApi::class)
    val fireCountryLatLng: StateFlow<LatLng?> =
        fireCountry.flatMapLatest {
            flow {
                emit(FireCountry.fireCountryLatLngMap[it])
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            // if the fetch time doesn't exist, set the time to yesterday so we can fetch data
            initialValue = FireCountry.fireCountryLatLngMap[sharedPreference.getString(FIRE_LAST_SELECTED_COUNTRY, "United States")]
        )

    // 4. last data fetch time
    @OptIn(ExperimentalCoroutinesApi::class)
    private val fireCountryDataFetchTime: StateFlow<Long> =
        fireCountry.flatMapLatest {
            sharedPreference.getLongFlowForKey(getKeyForDataFetchTime(it))
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            // if the fetch time doesn't exist, set the time to yesterday so we can fetch data
            initialValue = getCurrentTimeForFire()
        )

    // 5. fire data source type
    private val _fireSourceType = MutableStateFlow<Int>(ALL)
    private val fireSourceType: StateFlow<Int>
        get() = _fireSourceType.asStateFlow()

    // 6. list of fires to display in the screen
    @OptIn(ExperimentalCoroutinesApi::class)
    val listOfFiresInCountry: StateFlow<List<FireEntity>> =
        combine(
            fireCountryDataFetchTime, fireCountry, fireSourceType,
        ) { fetchTime, country, sourceType ->
            Triple(fetchTime, country, sourceType)
        }.flatMapLatest {
            if (getCurrentTimeForFire() >= it.first + 86400000) {
                // fetch the latest data if a day is passed
                startWorker(it.second)
            }
            if (it.third == ALL) { // don't filter
                fireRepository.getFiresInCountryFromLocal(it.second)
            } else {
                fireRepository.getFiresInCountryWithSourceTypeFromLocal(it.second, it.third)
            }
        }.flowOn(Dispatchers.IO).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    // 7. selected fire
    private val _currFire = MutableStateFlow<FireEntity?>(null)
    val currFire: StateFlow<FireEntity?>
        get() = _currFire.asStateFlow()

    private val _prevFire = MutableStateFlow<FireEntity?>(null)
    val prevFire: StateFlow<FireEntity?>
        get() = _prevFire.asStateFlow()

    fun updateCurrFire(fireEntity: FireEntity?) {
        _prevFire.value = _currFire.value
        _currFire.value = fireEntity
    }

    // 8. country dialog state
    private val _countryDialogState = MutableStateFlow<FireCountryDialogState>(FireCountryDialogState())
    val countryDialogState: StateFlow<FireCountryDialogState>
        get() = _countryDialogState.asStateFlow()

    fun dismissCountryDialog() {
        _countryDialogState.update {
            it.copy(visible = false)
        }
    }

    fun turnOnCountryDialog() {
        _countryDialogState.update {
            it.copy(visible = true)
        }
    }

    fun updateFireCountry(countryKey: String) {
        _fireCountry.value = fireCountryMap[countryKey] ?: "USA"
        sharedPreference.edit().putString(FIRE_LAST_SELECTED_COUNTRY, countryKey).apply()
    }

    fun updateFireSourceType(type: String) {
        _fireSourceType.value = when (type) {
            FireSourceDropdownItem.ALL -> ALL
            FireSourceDropdownItem.VIIRS_SNPP_NRT -> VIIRS_SNPP_NRT
            FireSourceDropdownItem.VIIRS_NOAA20_NRT -> VIIRS_NOAA20_NRT
            FireSourceDropdownItem.VIIRS_NOAA21_NRT -> VIIRS_NOAA21_NRT
            else -> ALL
        }
    }

    private fun startWorker(country: String) {
        viewModelScope.launch {
            val workConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val workData = workDataOf(WORK_DATA_COUNTRY_KEY to country)

            val request = OneTimeWorkRequestBuilder<FireWorker>()
                .setInputData(workData)
                .setConstraints(workConstraints)
                .addTag(FIRE_WORK_TAG)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
                    TimeUnit.MILLISECONDS
                ).build()

            workManager.enqueueUniqueWork(
                "work_name_fire",
                ExistingWorkPolicy.KEEP,
                request
            )
        }
    }

    private fun SharedPreferences.getLongFlowForKey(keyForLong: String): Flow<Long> =
        callbackFlow<Long> {
            val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                if (keyForLong == key) {
                    trySend(getLong(key, getYesterdayTimeForFire()))
                }
            }
            registerOnSharedPreferenceChangeListener(listener)

            // if you want to emit an initial pre-existing value
            if (contains(keyForLong)) {
                send(getLong(keyForLong, getYesterdayTimeForFire()))
            } else {
                send(getYesterdayTimeForFire()) // send yesterday time if key doesn't exist
            }
            awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
        }.buffer(Channel.UNLIMITED) // so trySend never fails

    private fun getKeyForDataFetchTime(country: String): String =
        country + "Key" //add suffix for country key


    fun setFireCountryFetchTime() {
        sharedPreference.edit().putLong(getKeyForDataFetchTime(fireCountry.value), getCurrentTimeForFire()).apply()
    }
}