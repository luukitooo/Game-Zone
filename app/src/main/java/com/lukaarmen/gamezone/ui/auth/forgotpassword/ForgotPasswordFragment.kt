package com.lukaarmen.gamezone.ui.auth.forgotpassword

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.findTopNavController
import com.lukaarmen.gamezone.databinding.FragmentForgotPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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
                    Snackbar.make(binding.root, "Invalid E-mail address...", Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }


}