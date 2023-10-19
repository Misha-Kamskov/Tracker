package com.example.tracker.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.tracker.R
import com.example.tracker.databinding.ActivityTrackerBinding
import com.example.tracker.auth.login.LoginContract
import com.example.tracker.auth.splash.SplashContract
import com.example.tracker.ui.tracker.TrackerContract
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackerActivity : AppCompatActivity(), SplashContract.Host, LoginContract.Host,
    TrackerContract.Host {

    private var navController: NavController? = null
    private var bind: ActivityTrackerBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = ActivityTrackerBinding.inflate(layoutInflater)
        setContentView(bind?.root)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_tracker)
    }

    override fun proceedAuthToMainScreen() {
        navController?.navigate(R.id.action_loginFragment_to_trackerFragment)
    }

    override fun proceedSplashToAuthScreen() {
        navController?.navigate(R.id.action_splashFragment_to_loginFragment)
    }

    override fun proceedSplashToMainScreen() {
        navController?.navigate(R.id.action_splashFragment_to_trackerFragment)
    }

    override fun proceedLocationToLoginScreen() {
        navController?.navigate(R.id.action_trackerFragment_to_loginFragment)
    }

}
