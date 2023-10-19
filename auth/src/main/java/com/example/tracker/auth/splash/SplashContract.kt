package com.example.tracker.auth.splash

import com.example.tracker.mvi.fragments.FragmentContract

class SplashContract {

    interface ViewModel : FragmentContract.ViewModel<View>

    interface View : FragmentContract.View {
        fun proceedToAuthScreen()
        fun proceedToMainScreen()
    }

    interface Host : FragmentContract.Host {
        fun proceedSplashToAuthScreen()
        fun proceedSplashToMainScreen()
    }
}
