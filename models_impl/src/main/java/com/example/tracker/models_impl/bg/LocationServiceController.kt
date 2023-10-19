package com.example.tracker.models_impl.bg

import com.example.tracker.models.LocationsRepository
import com.example.tracker.models.bus.StatusManager
import com.example.tracker.models.gps.LocationSource
import com.example.tracker.models.bg.work.WorkScheduler
import com.example.tracker.models.bg.LocationController
import com.example.tracker.models.prefs.TrackerPrefs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LocationServiceController(
    private val location: LocationSource,
    private val gpsStateCache: StatusManager,
    private val locationRepository: LocationsRepository,
    private val uploadWorkScheduler: WorkScheduler,
    private val prefs: TrackerPrefs
) : LocationController {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        gpsStateCache.setServiceStatus(true)

        serviceScope.launch {
            prefs.putTrackerStatus(true)
        }

        var locationCount = 0

        location.observeLocations()
            .catch { e -> e.printStackTrace() }
            .onEach {
                try {
                    gpsStateCache.setLocationsCounter(++locationCount)
                    locationRepository.saveLocation(it)
                    locationRepository.syncTrackerLocations()
                } catch (e: Exception) {
                    e.printStackTrace()
                    uploadWorkScheduler.scheduleSync()
                }
            }.launchIn(serviceScope)

        location.getGpsStatusFlow()
            .catch { e -> e.printStackTrace() }
            .onEach { gpsStateCache.setGpsStatus(it) }
            .launchIn(serviceScope)
    }

    override fun onDestroy() {

        serviceScope.launch {
            prefs.putTrackerStatus(false)
        }

        gpsStateCache.setServiceStatus(false)
        gpsStateCache.setLocationsCounter(0)
        serviceScope.cancel()

    }

}
