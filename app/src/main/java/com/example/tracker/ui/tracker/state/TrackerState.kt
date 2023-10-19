package com.example.tracker.ui.tracker.state

import com.example.tracker.mvi.states.AbstractState
import com.example.tracker.ui.tracker.TrackerContract

class TrackerState(
    val serviceRunning: Boolean = false,
    private val isGpsEnable: Boolean,
    private val locationsCounter: Int
) : AbstractState<TrackerContract.View, TrackerState>() {

    override fun visit(screen: TrackerContract.View) {
        screen.showTrackerState(serviceRunning, isGpsEnable, locationsCounter)
    }

}
