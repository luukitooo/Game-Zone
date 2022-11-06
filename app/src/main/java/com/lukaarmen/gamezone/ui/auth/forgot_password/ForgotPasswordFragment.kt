package com.lukaarmen.gamezone.ui.auth.forgot_password

import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extension.doInBackground
import com.lukaarmen.gamezone.common.extension.findTopNavController
import com.lukaarmen.gamezone.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>(
    FragmentForgotPasswordBinding::inflate
) {

    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun init() {
        return
    }

    override fun listeners() = with(binding) {
        btnSendLink.setOnClickListener {
            doInBackground {
                viewModel.resetPasswordByEmail(
                    edtEmail.text.toString()
                )
            }
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.resetSuccessFlow.collect { isSuccessful ->
                if (isSuccessful) {
                    findTopNavController().popBackStack()
                } else {
                    Snackbar.make(binding.root, getString(R.string.invalid_email_address), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }


}