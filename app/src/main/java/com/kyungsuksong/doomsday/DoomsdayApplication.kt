package com.kyungsuksong.doomsday

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class DoomsdayApplication : Application(), Configuration.Provider {

    // check "https://developer.android.com/reference/androidx/hilt/work/HiltWorker"
    // for detailed information about the HiltWorker
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

}