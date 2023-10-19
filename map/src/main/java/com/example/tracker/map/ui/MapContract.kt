package com.example.tracker.map.ui

import com.example.tracker.models.locations.Location
import com.example.tracker.mvi.fragments.FragmentContract

class MapContract {

    interface ViewModel : FragmentContract.ViewModel<View> {

        fun getFilteredLocations(startDate: Long, endDate: Long = MapViewModel.MILLISECONDS_PER_DAY)

        fun singOut()

    }

    interface View : FragmentContract.View {

        fun proceedToLoginScreen()

        fun showMessage(messageId: Int)

        fun showMapState(locationsList: List<Location>, isGetLocationsRunning: Boolean)

    }

    interface Host : FragmentContract.Host {
        fun proceedLocationToLoginScreen()
    }

}
