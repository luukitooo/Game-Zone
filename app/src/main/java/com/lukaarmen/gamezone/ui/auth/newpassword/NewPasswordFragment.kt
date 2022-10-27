package com.lukaarmen.gamezone.ui.auth.newpassword

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.databinding.FragmentNewPasswordBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewPasswordFragment : BaseFragment<FragmentNewPasswordBinding>(
    FragmentNewPasswordBinding::inflate
) {

    private val viewModel by viewModels<NewPasswordViewModel>()
    override fun init() {
        return
    }

    override fun listeners() = with(binding) {
        btnConfirmPassword.setOnClickListener {
            viewModel.updatePassword(
                oldPassword = edtConfirmPassword.text.toString(),
                newPassword = edtPassword.text.toString(),
                repeatNewPassword = edtRepeatPassword.text.toString()
            )
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.passwordChangeFlow.collect { isSuccess ->
                when (isSuccess) {
                    true -> successful()
                    false -> failure()
                }
            }
        }
    }

    private fun successful() {
        Snackbar.make(binding.root, "Password successfully changed", Snackbar.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    private fun failure() {
        Snackbar.make(binding.root, "Check your credentials", Snackbar.LENGTH_SHORT).show()
    }

}