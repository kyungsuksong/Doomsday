package com.kyungsuksong.doomsday.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kyungsuksong.doomsday.repo.AsteroidRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class AsteroidWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val asteroidRepository: AsteroidRepository
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            asteroidRepository.fetchAndSaveAsteroids()
            asteroidRepository.fetchAndSavePicture()
            Result.success()
        } catch(e : Exception) {
            Result.failure()
        }
    }
}