package com.example.tracker.ui.tracker

import com.example.tracker.mvi.fragments.FragmentContract

class TrackerContract {

    interface ViewModel : FragmentContract.ViewModel<View> {

        fun singOut()

        fun clearLocationsAndSignOut()

        fun scheduleUploadLocations()

    }

    interface View : FragmentContract.View {

        fun showTrackerState(serviceRunning: Boolean, isGpsEnable: Boolean, locationsCounter: Int)

        fun startStopService(act: String)

        fun proceedToLoginScreen()

        fun showLogOutDialog()

    }

    interface Host : FragmentContract.Host {
        fun proceedLocationToLoginScreen()
    }
}
