package com.example.tracker.ui.signout

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.tracker.R
import com.example.tracker.databinding.FragmentSignOutBinding

class SignOutFragment : DialogFragment(), View.OnClickListener {

    private var bind: FragmentSignOutBinding? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bind = FragmentSignOutBinding.inflate(layoutInflater)
        val dialog = Dialog(requireContext())

        bind?.let {
            dialog.setContentView(it.root)
            it.btDialogYes.setOnClickListener(this)
            it.btDialogNo.setOnClickListener(this)
        }

        dialog.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )

        dialog.setCancelable(false)
        return dialog
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.bt_dialog_yes -> {
                sendIsSignOut(true)
                dismiss()
            }

            R.id.bt_dialog_no -> {
                sendIsSignOut(false)
                dismiss()
            }
        }
    }

    private fun sendIsSignOut(isSignOut: Boolean) {
        val result = Bundle()
        result.putBoolean(SIGN_OUT, isSignOut)
        parentFragmentManager.setFragmentResult(SIGN_OUT_REQUEST_KEY, result)
    }

    companion object {
        const val SIGN_OUT = "SIGN_OUT"
        const val SIGN_OUT_REQUEST_KEY = "SIGN_OUT_REQUEST_KEY"
    }

}