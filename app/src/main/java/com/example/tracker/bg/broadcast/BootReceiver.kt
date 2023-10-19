package com.example.tracker.bg.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.tracker.bg.LocationService
import com.example.tracker.models.prefs.TrackerPrefs
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject
    lateinit var prefs: TrackerPrefs

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onReceive(context: Context?, intent: Intent?) {
        coroutineScope.launch {
            if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
                val trackerStatus = prefs.getTrackerStatus()
                if (trackerStatus) {
                    Intent(context, LocationService::class.java).apply {
                        action = LocationService.START
                        context?.let { ContextCompat.startForegroundService(it, this) }
                    }
                }
            }
        }
    }
}
