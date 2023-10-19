package com.example.tracker.auth.splash.state

import com.example.tracker.mvi.states.AbstractEffect
import com.example.tracker.auth.splash.SplashContract

open class SplashEffect : AbstractEffect<SplashContract.View>() {

    class ProceedToLoginScreen : SplashEffect() {
        override fun handle(screen: SplashContract.View) {
            screen.proceedToAuthScreen()
        }
    }

    class ProceedToTrackerScreen : SplashEffect() {
        override fun handle(screen: SplashContract.View) {
            screen.proceedToMainScreen()
        }
    }
}
