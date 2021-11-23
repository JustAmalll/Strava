package com.skillbox.strava

import android.app.Application
import android.os.Build
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.skillbox.core.notification.NotificationChannels
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {
    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        initLog()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            NotificationChannels.createNotificationChannel(this)
    }

    private fun initLog() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}