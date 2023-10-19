package com.example.tracker.auth.login

import android.util.Patterns
import androidx.lifecycle.viewModelScope
import com.example.auth.R
import com.example.tracker.models.auth.Auth
import com.example.tracker.mvi.MviViewModel
import com.example.tracker.auth.login.state.LoginEffect
import com.example.tracker.auth.login.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authNetwork: Auth
) : MviViewModel<LoginContract.View, LoginState>(), LoginContract.ViewModel {

    private val minPassLength = 6

    private fun validateCredentials(
        userEmail: String?, userPass: String? = null, userPass2: String? = null
    ): Boolean {
        var loginError: Int = R.string.empty_error_message
        var passError: Int = R.string.empty_error_message
        userEmail?.let {
            if (userEmail.isEmpty()) {
                loginError = R.string.email_is_empty
            } else if (!isEmailValid(userEmail)) {
                loginError = R.string.write_correct_email
            }
        }
        userPass?.let {
            if (isPassLengthEnough(userPass)) {
                passError = R.string.to_short_password
            }
            userPass2?.let {
                if (!arePasswordsMatch(userPass, userPass2)) {
                    passError = R.string.passwords_mismatch
                }
            }
        } ?: kotlin.run {
            userPass2?.let {
                passError = R.string.passwords_mismatch
            }
        }
        setState(LoginState(loginError, passError))
        return loginError != R.string.empty_error_message || passError != R.string.empty_error_message
    }

    override fun signIn(userEmail: String, userPass: String) {
        if (validateCredentials(userEmail, userPass)) {
            return
        }
        viewModelScope.launch {
            try {
                authNetwork.signIn(userEmail, userPass)
                setState(LoginState())
                setEffect(LoginEffect.NavigateAfterSignIn())
            } catch (e: Exception) {
                e.printStackTrace()
                setState(LoginState(loginError = R.string.login_failed))
            }
        }
    }

    override fun signUp(userEmail: String, userPassFirst: String, userPassSecond: String) {
        if (validateCredentials(userEmail, userPassFirst, userPassSecond)) {
            return
        }
        viewModelScope.launch {
            try {
                authNetwork.signUp(userEmail, userPassFirst)
                setEffect(LoginEffect.ShowSuccessMessage(R.string.registration_completed))
                setState(LoginState())
            } catch (e: Exception) {
                e.printStackTrace()
                setState(LoginState(loginError = R.string.registration_failed))
            }
        }
    }

    override fun forgotPassword(userEmail: String) {
        if (validateCredentials(userEmail)) {
            return
        }
        viewModelScope.launch {
            try {
                authNetwork.forgotPassword(userEmail)
                setEffect(LoginEffect.ShowSuccessMessage(R.string.check_your_email))
                setState(LoginState())
            } catch (e: Exception) {
                setState(LoginState(loginError = R.string.something_went_wrong_password_wasnt_reset))
            }
        }
    }

    private fun isEmailValid(userEmail: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()
    }

    private fun isPassLengthEnough(userPass: String): Boolean {
        return userPass.length <= minPassLength
    }

    private fun arePasswordsMatch(firstPass: String, secondPass: String): Boolean {
        return firstPass == secondPass
    }

}
