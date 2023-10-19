package com.example.tracker.bg

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.tracker.models.bg.LocationController
import com.example.tracker.R
import com.example.tracker.ui.TrackerActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service() {

    @Inject
    lateinit var controller: LocationController

    private val builder = NotificationCompat.Builder(this, TRACKER_CHANNEL_ID)

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            START -> start()
            STOP -> stop()
        }
        return START_NOT_STICKY
    }

    private fun start() {
        startForeground(TRACKER_NOTIFICATION_ID, createAndUpdateNotification(START))
        controller.onCreate()
    }

    private fun stop() {
        startForeground(TRACKER_NOTIFICATION_ID, createAndUpdateNotification(STOP))
        stopForeground(STOP_FOREGROUND_DETACH)
        controller.onDestroy()
        stopSelf()
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                TRACKER_CHANNEL_ID, TRACKER_CHANNEL_ID,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun proceedToTrackerFragment(): PendingIntent {
        return PendingIntent.getActivity(
            this, 0, Intent(this, TrackerActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun createAndUpdateNotification(action: String): Notification {
        return builder.clearActions()
            .setContentTitle(getString(R.string.tracker))
            .setContentText(getString(if (action == START) R.string.collects_locations else R.string.service_state_idle))
            .setSmallIcon(R.drawable.img_tracker_collects_locations)
            .setContentIntent(proceedToTrackerFragment())
            .addAction(
                if (action == START) R.drawable.ic_stop else R.drawable.ic_start,
                if (action == START) getString(R.string.stop) else getString(R.string.start),
                if (action == START) startStopIntent(STOP) else startStopIntent(START)
            )
            .setOngoing(false)
            .build()
    }

    private fun startStopIntent(action: String): PendingIntent {
        val intent = Intent(this, LocationService::class.java)
        intent.action = action
        return PendingIntent.getService(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    companion object {
        const val START = "ACTION_START"
        const val STOP = "ACTION_STOP"
        const val TRACKER_CHANNEL_ID = "TRACKER_CHANNEL_ID"
        const val TRACKER_NOTIFICATION_ID = 1
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
