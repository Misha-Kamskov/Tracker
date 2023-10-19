package com.example.tracker.map.ui

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import com.example.tracker.map.R
import com.example.tracker.models.auth.Auth
import com.example.tracker.models.LocationsRepository
import com.example.tracker.mvi.MviViewModel
import com.example.tracker.map.ui.state.MapEffect
import com.example.tracker.map.ui.state.MapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val authNetwork: Auth,
    private val locationsRepository: LocationsRepository
) : MviViewModel<MapContract.View, MapState>(), MapContract.ViewModel {

    private val exceptionHandler = CoroutineExceptionHandler { _, _ ->
        setEffect(MapEffect.ShowMessage(R.string.download_failed))
        setState(MapState(listOf(), false))
    }

    override fun onStateChanged(event: Lifecycle.Event) {
        super.onStateChanged(event)
        if (event == Lifecycle.Event.ON_CREATE) {
            getFilteredLocations(startDate = getTodayTime())
        }
    }

    override fun getFilteredLocations(startDate: Long, endDate: Long) {
        viewModelScope.launch(exceptionHandler) {
            setState(MapState(listOf(), true))
            val list = locationsRepository.getMapLocations(startDate, endDate)
            if (list.isEmpty()) {
                setEffect(MapEffect.ShowMessage(R.string.any_locations))
                setState(MapState(listOf(), false))
            } else {
                setState(MapState(list, false))
            }
        }
    }

    override fun singOut() {
        authNetwork.signOut()
        viewModelScope.launch {
            locationsRepository.clearLocations()
            setEffect(MapEffect.NavigateAfterLogOut())
        }
    }

    private fun getTodayTime(): Long {
        return System.currentTimeMillis() - MILLISECONDS_PER_DAY
    }

    companion object {
        const val MILLISECONDS_PER_DAY = 86_400_000L
    }

}
