package com.kyungsuksong.doomsday.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.kyungsuksong.doomsday.repo.FireRepository
import com.kyungsuksong.doomsday.util.WORK_DATA_COUNTRY_KEY
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class FireWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val fireRepository: FireRepository
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val country: String = inputData.getString(WORK_DATA_COUNTRY_KEY) ?: "USA"
            fireRepository.fetchAndSaveFiresFromViirSnppNrtSource(country)
            fireRepository.fetchAndSaveFiresFromViirNoaa20NrtSource(country)
            fireRepository.fetchAndSaveFiresFromViirNoaa21NrtSource(country)
            Result.success()
        } catch(e: Exception) {
            Result.failure()
        }
    }
}