package com.leodeleon.hikepix

import android.annotation.TargetApi
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp
import splitties.resources.appStr
import splitties.systemservices.notificationManager
import timber.log.Timber
import timber.log.Timber.*


@HiltAndroidApp
class App: Application() {
    companion object {
        const val CHANNEL_ID = "com.leodeleon.hikepix.updates"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            appStr(R.string.hike_updates),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(serviceChannel)
    }
}