package com.example.tracker.ui.tracker.state

import com.example.tracker.mvi.states.AbstractEffect
import com.example.tracker.ui.tracker.TrackerContract

open class TrackerEffect : AbstractEffect<TrackerContract.View>() {

    class NavigateAfterLogOut : TrackerEffect() {
        override fun handle(screen: TrackerContract.View) {
            screen.proceedToLoginScreen()
        }
    }

    class ShowLogOutDialog : TrackerEffect() {
        override fun handle(screen: TrackerContract.View) {
            screen.showLogOutDialog()
        }
    }

}
