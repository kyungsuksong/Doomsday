package com.kyungsuksong.doomsday.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kyungsuksong.doomsday.repo.EarthquakeRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class EarthquakeWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val earthquakeRepository: EarthquakeRepository
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            earthquakeRepository.fetchAndSaveEarthquakes()
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}