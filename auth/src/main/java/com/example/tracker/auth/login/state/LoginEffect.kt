package com.example.tracker.auth.login.state

import com.example.tracker.mvi.states.AbstractEffect
import com.example.tracker.auth.login.LoginContract

open class LoginEffect : AbstractEffect<LoginContract.View>() {

    class ShowSuccessMessage(private val messageId: Int?) : LoginEffect() {
        override fun handle(screen: LoginContract.View) {
            screen.showPopUpError(messageId)
        }
    }

    class NavigateAfterSignIn : LoginEffect() {
        override fun handle(screen: LoginContract.View) {
            screen.proceedAuthToMainScreen()
        }
    }
}
