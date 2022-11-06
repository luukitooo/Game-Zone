package com.lukaarmen.gamezone.ui.auth.new_password

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extension.doInBackground
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
            if (etConfirmPassword.text?.isNotEmpty() == true) {
                viewModel.updatePasswordByEmail(
                    email = etConfirmPassword.text.toString()
                )
            }
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.passwordChangeSuccessFlow.collect { isSuccessful ->
                if (isSuccessful) {
                    findNavController().popBackStack()
                    Snackbar.make(binding.root, getString(R.string.check_your_email), Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.root, getString(R.string.cant_send_confirmation_email), Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

}