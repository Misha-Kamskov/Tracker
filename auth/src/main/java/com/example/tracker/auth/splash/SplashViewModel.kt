package com.example.tracker.auth.splash

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import com.example.tracker.models.auth.Auth
import com.example.tracker.mvi.MviViewModel
import com.example.tracker.auth.splash.state.SplashEffect
import com.example.tracker.auth.splash.state.SplashState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authNetwork: Auth
) : MviViewModel<SplashContract.View, SplashState>(), SplashContract.ViewModel {

    private val splashDelay = 1000L
    private val handler = CoroutineExceptionHandler { _, throwable ->
        throwable.printStackTrace()
    }

    override fun onStateChanged(event: Lifecycle.Event) {
        super.onStateChanged(event)
        if (event == Lifecycle.Event.ON_CREATE) {
            viewModelScope.launch(handler) {
                delay(splashDelay)
                if (authNetwork.isSignedIn()) {
                    setEffect(SplashEffect.ProceedToTrackerScreen())
                } else {
                    setEffect(SplashEffect.ProceedToLoginScreen())
                }
            }
        }
    }

}
