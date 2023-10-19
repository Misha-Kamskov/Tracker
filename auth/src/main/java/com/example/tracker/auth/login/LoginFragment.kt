package com.example.tracker.auth.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.auth.databinding.FragmentLoginBinding
import com.example.auth.R
import com.example.tracker.mvi.fragments.HostedFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment :
    HostedFragment<LoginContract.View, LoginContract.ViewModel, LoginContract.Host>(),
    LoginContract.View, View.OnClickListener {

    private var bind: FragmentLoginBinding? = null

    override fun createModel(): LoginViewModel {
        val viewModel: LoginViewModel by viewModels()
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        bind = FragmentLoginBinding.inflate(inflater, container, false)
        return bind?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bind?.btSingInUp?.setOnClickListener(this)
        bind?.tvForgotPassword?.setOnClickListener(this)
        bind?.tvSingInUp?.setOnClickListener(this)
        bind?.ibBack?.setOnClickListener(this)
    }

    override fun showLoginError(messageId: Int) {
        bind?.inputFieldUserName?.error = getString(messageId)
    }

    override fun showPasswordError(messageId: Int) {
        bind?.inputFieldPassword?.error = getString(messageId)
    }

    override fun showPopUpError(messageId: Int?) {
        messageId?.let {
            view?.let { Snackbar.make(it, getString(messageId), Snackbar.LENGTH_LONG).show() }
        }
    }

    override fun proceedAuthToMainScreen() {
        fragmentHost?.proceedAuthToMainScreen()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.bt_sing_in_up -> buttonToggle()
            R.id.tv_forgot_password -> clickOnForgotPassword()
            R.id.tv_sing_in_up -> clickOnSingInUp()
            R.id.ib_back -> backToLoginViews()
        }
    }

    private fun buttonToggle() {
        val userEmail = bind?.edUserEmail?.text.toString()
        val userPassword = bind?.edPassword?.text.toString()
        val confirmPassword = bind?.edConfirmPassword?.text.toString()

        when (bind?.btSingInUp?.text.toString()) {
            resources.getString(R.string.sing_in) -> {
                model?.signIn(userEmail, userPassword)
            }

            resources.getString(R.string.sing_up) -> {
                model?.signUp(userEmail, userPassword, confirmPassword)
            }

            else -> model?.forgotPassword(userEmail)
        }
    }

    private fun clickOnForgotPassword() {
        doInvisibleNonForgotPasswordFields()
        bind?.btSingInUp?.text = resources.getString(R.string.send_new_password)
        bind?.tvSignTitle?.text = resources.getString(R.string.forgot_password)
        bind?.ibBack?.visibility = View.VISIBLE
    }

    private fun clickOnSingInUp() {
        if (bind?.tvSingInUp?.text == (resources.getString(R.string.question_sing_up))) {
            setUpLogInUpViewsProperties(
                View.GONE, View.VISIBLE,
                resources.getString(R.string.sing_up),
                resources.getString(R.string.question_sing_in),
            )
        } else {
            setUpLogInUpViewsProperties(
                View.VISIBLE, View.GONE,
                resources.getString(R.string.sing_in),
                resources.getString(R.string.question_sing_up),
            )
        }
    }

    private fun setUpLogInUpViewsProperties(
        visibilityForgotPassword: Int,
        visibilityConfirmPassword: Int,
        titleOfToolbarAndButton: String,
        bottomTextMenu: String,
    ) {
        bind?.tvForgotPassword?.visibility = visibilityForgotPassword
        bind?.inputFieldConfirmPassword?.visibility = visibilityConfirmPassword
        bind?.ibBack?.visibility = View.GONE
        bind?.btSingInUp?.text = titleOfToolbarAndButton
        bind?.tvSingInUp?.text = bottomTextMenu
        bind?.tvSignTitle?.text = titleOfToolbarAndButton
    }

    private fun backToLoginViews() {
        bind?.inputFieldPassword?.visibility = View.VISIBLE
        bind?.tvForgotPassword?.visibility = View.VISIBLE
        bind?.tvSingInUp?.visibility = View.VISIBLE
        bind?.inputFieldConfirmPassword?.visibility = View.GONE
        bind?.ibBack?.visibility = View.GONE
        bind?.btSingInUp?.text = resources.getString(R.string.sing_up)
        bind?.tvSingInUp?.text = resources.getString(R.string.question_sing_up)
        bind?.tvSignTitle?.text = resources.getString(R.string.sing_up)
    }

    private fun doInvisibleNonForgotPasswordFields() {
        bind?.inputFieldPassword?.visibility = View.GONE
        bind?.tvForgotPassword?.visibility = View.GONE
        bind?.tvSingInUp?.visibility = View.GONE
    }

}
