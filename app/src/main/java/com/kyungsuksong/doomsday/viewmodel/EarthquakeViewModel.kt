package com.kyungsuksong.doomsday.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.kyungsuksong.doomsday.compose.earthquake.EarthquakeDurationDropdownItem
import com.kyungsuksong.doomsday.compose.earthquake.EarthquakeMagnitudeDropdownItem
import com.kyungsuksong.doomsday.data.room.EarthquakeEntity
import com.kyungsuksong.doomsday.repo.EarthquakeRepository
import com.kyungsuksong.doomsday.util.EARTHQUAKE_WORK_TAG
import com.kyungsuksong.doomsday.util.earthquake_today
import com.kyungsuksong.doomsday.worker.EarthquakeWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

object EarthquakeDurationType {
    const val DAYS_15 = 0
    const val WEEK = 1
    const val DAYS_3 = 2
}

object EarthquakeMagnitudeType {
    const val ALL = 0
    const val GREATER_THAN_8 = 1
    const val BETWEEN_7_AND_8 = 2
    const val BETWEEN_6_AND_7 = 3
    const val BETWEEN_5P5_AND_6 = 4
    const val BETWEEN_2P5_AND_5P5 = 5
    const val LESS_THAN_2P5 = 6
}

@HiltViewModel
class EarthquakeViewModel @Inject constructor(
    earthquakeRepository: EarthquakeRepository,
    private val workManager: WorkManager
): ViewModel() {

    val workState: SharedFlow<List<WorkInfo>> =
        workManager
            .getWorkInfosByTagFlow(EARTHQUAKE_WORK_TAG)
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            )

    private var _earthquakeMagnitudeType = MutableStateFlow<Int>(EarthquakeMagnitudeType.ALL)
    private val earthquakeMagnitudeType: StateFlow<Int>
        get() = _earthquakeMagnitudeType.asStateFlow()

    private var _earthquakeDurationType = MutableStateFlow<Int>(EarthquakeDurationType.DAYS_15)
    private val earthquakeDurationType: StateFlow<Int>
        get() = _earthquakeDurationType.asStateFlow()

    private val durationList = listOf(
        listOf(earthquake_today - 1296000000, earthquake_today),
        listOf(earthquake_today - 604800000, earthquake_today),
        listOf(earthquake_today - 259200000, earthquake_today)
    )

    private val magList = listOf(
        listOf(0.0, 100.0),
        listOf(8.0, 100.0),
        listOf(7.0, 7.9999999),
        listOf(6.0, 6.9999999),
        listOf(5.5, 5.9999999),
        listOf(2.5, 5.4999999),
        listOf(0.0, 2.4999999),
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val listOfEarthquakes: StateFlow<List<EarthquakeEntity>> =
        combine(
            earthquakeMagnitudeType, earthquakeDurationType
        ) { magType, durationType ->
            magType to durationType
        }.flatMapLatest { pair ->
            earthquakeRepository.getListOfEarthquakesFromLocal(
                startMag = magList[pair.first][0],
                endMag = magList[pair.first][1],
                startTime = durationList[pair.second][0],
                endTime = durationList[pair.second][1]
            )
        }.flowOn(Dispatchers.IO).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    private val _currEarthquake = MutableStateFlow<EarthquakeEntity?>(null)
    val currEarthquake: StateFlow<EarthquakeEntity?>
        get() = _currEarthquake.asStateFlow()

    private val _prevEarthquake = MutableStateFlow<EarthquakeEntity?>(null)
    val prevEarthquake: StateFlow<EarthquakeEntity?>
        get() = _prevEarthquake.asStateFlow()

    fun updateCurrEarthquake(earthquakeEntity: EarthquakeEntity?) {
        _prevEarthquake.value = _currEarthquake.value
        _currEarthquake.value = earthquakeEntity
    }

    fun updateEarthquakeDurationType(type: String) {
        _earthquakeDurationType.value = when (type) {
            EarthquakeDurationDropdownItem.DAYS_15 -> EarthquakeDurationType.DAYS_15
            EarthquakeDurationDropdownItem.WEEK -> EarthquakeDurationType.WEEK
            EarthquakeDurationDropdownItem.DAYS_3 -> EarthquakeDurationType.DAYS_3
            else -> EarthquakeDurationType.DAYS_15
        }
    }

    fun updateEarthquakeMagType(type: String) {
        _earthquakeMagnitudeType.value = when (type) {
            EarthquakeMagnitudeDropdownItem.ALL -> EarthquakeMagnitudeType.ALL
            EarthquakeMagnitudeDropdownItem.GREATER_THAN_8 -> EarthquakeMagnitudeType.GREATER_THAN_8
            EarthquakeMagnitudeDropdownItem.BETWEEN_7_AND_8 -> EarthquakeMagnitudeType.BETWEEN_7_AND_8
            EarthquakeMagnitudeDropdownItem.BETWEEN_6_AND_7 -> EarthquakeMagnitudeType.BETWEEN_6_AND_7
            EarthquakeMagnitudeDropdownItem.BETWEEN_5P5_AND_6 -> EarthquakeMagnitudeType.BETWEEN_5P5_AND_6
            EarthquakeMagnitudeDropdownItem.BETWEEN_2P5_AND_5P5 -> EarthquakeMagnitudeType.BETWEEN_2P5_AND_5P5
            EarthquakeMagnitudeDropdownItem.LESS_THAN_2P5 -> EarthquakeMagnitudeType.LESS_THAN_2P5
            else -> EarthquakeMagnitudeType.ALL
        }
    }

    fun startWorker() {
        viewModelScope.launch {
            val workConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<EarthquakeWorker>(24, TimeUnit.HOURS)
                .setConstraints(workConstraints)
                .addTag(EARTHQUAKE_WORK_TAG)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
                    TimeUnit.MILLISECONDS
                ).build()

            workManager.enqueueUniquePeriodicWork(
                "work_name_earthquake",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}