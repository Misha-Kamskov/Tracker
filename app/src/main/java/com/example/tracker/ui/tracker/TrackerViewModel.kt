package com.example.tracker.ui.tracker

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import com.example.tracker.models.LocationsRepository
import com.example.tracker.models.bg.work.WorkScheduler
import com.example.tracker.models.auth.Auth
import com.example.tracker.models.bus.StatusManager
import com.example.tracker.mvi.MviViewModel
import com.example.tracker.ui.tracker.state.TrackerEffect
import com.example.tracker.ui.tracker.state.TrackerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

@HiltViewModel
class TrackerViewModel @Inject constructor(
    private val authNetwork: Auth,
    private val gpsStateCache: StatusManager,
    private val locationsRepository: LocationsRepository,
    private val worker: WorkScheduler
) : MviViewModel<TrackerContract.View, TrackerState>(), TrackerContract.ViewModel {

    override fun onStateChanged(event: Lifecycle.Event) {
        super.onStateChanged(event)
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModelScope.launch {
                combine(
                    gpsStateCache.getServiceStatus(),
                    gpsStateCache.getGpsStatus(),
                    gpsStateCache.getLocationsCounter()
                ) { servStatus, gpsStatus, counter ->
                    TrackerState(servStatus, gpsStatus, counter)
                }.collect { newState ->
                    setState(newState)
                }
            }
        }
    }

    override fun clearLocationsAndSignOut() {
        viewModelScope.launch {
            locationsRepository.clearLocations()
            exit()
        }
    }

    override fun scheduleUploadLocations() {
        worker.scheduleSync()
    }

    override fun singOut() {
        viewModelScope.launch {
            try {
                withTimeout(DELAY) {
                    locationsRepository.syncTrackerLocations()
                }
                exit()
            } catch (e: Exception) {
                setEffect(TrackerEffect.ShowLogOutDialog())
            }
        }
    }

    private fun exit() {
        authNetwork.signOut()
        setEffect(TrackerEffect.NavigateAfterLogOut())
    }

    companion object {
        const val DELAY = 5000L
    }
}
