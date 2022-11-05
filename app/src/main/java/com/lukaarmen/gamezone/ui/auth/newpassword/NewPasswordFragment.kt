package com.lukaarmen.gamezone.ui.auth.newpassword

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.databinding.FragmentNewPasswordBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

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
                    Snackbar.make(binding.root, "Please check your email", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.root, "Can't send confirmation email, please try again", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

}