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
import com.kyungsuksong.doomsday.compose.asteroid.AsteroidTypeDropdownItem
import com.kyungsuksong.doomsday.data.room.AsteroidEntity
import com.kyungsuksong.doomsday.repo.AsteroidRepository
import com.kyungsuksong.doomsday.util.ASTEROID_WORK_TAG
import com.kyungsuksong.doomsday.worker.AsteroidWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

enum class AsteroidType {
    ONWARD,
    TODAY,
    PAST,
    ALL
}

@HiltViewModel
class AsteroidViewModel @Inject constructor(
    asteroidRepository: AsteroidRepository,
    private val workManager: WorkManager
): ViewModel() {

    val workState: SharedFlow<List<WorkInfo>> =
        workManager
            .getWorkInfosByTagFlow(ASTEROID_WORK_TAG)
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1
            )

    private val asteroidType = MutableStateFlow<AsteroidType>(AsteroidType.TODAY)

    private val listOfOnwardsAsteroid: Flow<List<AsteroidEntity>> =
        asteroidRepository.getListOfOnwardsAsteroidFromLocal()
    private val listOfTodayAsteroid: Flow<List<AsteroidEntity>> =
        asteroidRepository.getListOfTodayAsteroidFromLocal()
    private val listOfPastAsteroid: Flow<List<AsteroidEntity>> =
        asteroidRepository.getListOfPastAsteroidFromLocal()

    val filteredListOfAsteroid: StateFlow<List<AsteroidEntity>> =
        combine(
            listOfOnwardsAsteroid,
            listOfTodayAsteroid,
            listOfPastAsteroid,
            asteroidType
        ){ onwardsList, todayList, pastList, type ->
            when(type) {
                AsteroidType.ONWARD -> onwardsList
                AsteroidType.TODAY -> todayList
                AsteroidType.PAST -> pastList
                AsteroidType.ALL -> listOf(onwardsList, todayList, pastList).flatten()
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    val picOfDay = asteroidRepository.getPicOfDayFromLocal()

    fun updateAsteroidType(type: String) {
        asteroidType.value = when(type) {
            AsteroidTypeDropdownItem.ONWARD -> AsteroidType.ONWARD
            AsteroidTypeDropdownItem.TODAY -> AsteroidType.TODAY
            AsteroidTypeDropdownItem.PAST -> AsteroidType.PAST
            else -> AsteroidType.ALL
        }
    }

    fun startWorker() {
        viewModelScope.launch {
            val workConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val request = PeriodicWorkRequestBuilder<AsteroidWorker>(24, TimeUnit.HOURS)
                .setConstraints(workConstraints)
                .addTag(ASTEROID_WORK_TAG)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_PERIODIC_INTERVAL_MILLIS,
                    TimeUnit.MILLISECONDS
                ).build()

            workManager.enqueueUniquePeriodicWork(
                "work_name_asteroid",
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        }
    }
}