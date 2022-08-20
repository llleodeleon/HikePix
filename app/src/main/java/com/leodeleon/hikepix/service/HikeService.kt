package com.leodeleon.hikepix.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.leodeleon.hikepix.App
import com.leodeleon.hikepix.MainActivity
import com.leodeleon.hikepix.ui.util.pendingIntentFlags
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HikeService : Service() {
    companion object {
        private const val NOTIFICATION_ID = 1
        const val ACTION_START = "start"
        const val ACTION_STOP = "stop"
    }

    @Inject
    lateinit var manager: LocationManager

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        when (intent.action) {
            ACTION_START -> startHike()
            ACTION_STOP -> stopHike()
            else -> { /*Do nothing*/
            }
        }
        return START_NOT_STICKY
    }

    private fun startHike() {
        startForeground(NOTIFICATION_ID, createNotification())
        manager.requestLocation()
    }

    private fun stopHike() {
        stopForeground(true)
        stopSelf()
        manager.dispose()
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, pendingIntentFlags()
        )
        return NotificationCompat.Builder(this, App.CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .build()
    }
}