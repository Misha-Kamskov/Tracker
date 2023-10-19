package com.example.tracker.models_impl.bus

import com.example.tracker.models.bus.StatusManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class TrackerStatusManager : StatusManager {

    private val isServiceOn = MutableStateFlow(false)
    private val isGpsEnabled = MutableStateFlow(false)
    private val locationsCounter = MutableStateFlow(0)


    override fun getGpsStatus(): Flow<Boolean> = isGpsEnabled

    override fun setGpsStatus(isEnabled: Boolean) {
        isGpsEnabled.value = isEnabled
    }

    override fun getServiceStatus(): Flow<Boolean> = isServiceOn


    override fun setServiceStatus(isEnabled: Boolean) {
        isServiceOn.value = isEnabled
    }

    override fun getLocationsCounter(): Flow<Int> = locationsCounter


    override fun setLocationsCounter(count: Int) {
        locationsCounter.value = count
    }

}
