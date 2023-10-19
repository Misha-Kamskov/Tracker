package com.example.tracker.map.ui.state

import com.example.tracker.mvi.states.AbstractEffect
import com.example.tracker.map.ui.MapContract

open class MapEffect : AbstractEffect<MapContract.View>() {

    class NavigateAfterLogOut : MapEffect() {
        override fun handle(screen: MapContract.View) {
            screen.proceedToLoginScreen()
        }
    }

    class ShowMessage(private val messageId: Int) : MapEffect() {
        override fun handle(screen: MapContract.View) {
            screen.showMessage(messageId)
        }
    }

}
