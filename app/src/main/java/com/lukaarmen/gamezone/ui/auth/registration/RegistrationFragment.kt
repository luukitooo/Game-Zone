package com.lukaarmen.gamezone.ui.auth.registration

import android.util.Patterns
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extension.areLinesEmpty
import com.lukaarmen.gamezone.common.extension.doInBackground
import com.lukaarmen.gamezone.databinding.FragmentRegistrationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>(
    FragmentRegistrationBinding::inflate
) {

    private val viewModel: RegistrationViewModel by viewModels()

    override fun init() {
        return
    }

    override fun listeners() = with(binding) {
        btnCreate.setOnClickListener {
            handleRegistrationRequest()
        }

    }

    override fun observers() {
        doInBackground {
            viewModel.registrationSuccessFlow.collect { isSuccessful ->
                handleRegistrationResponse(isSuccessful)
            }
        }
    }

    private fun handleRegistrationRequest(): Unit = with(binding) {
        if (!areLinesEmpty(etUsername, etEmail, etPassword, etRepeatPassword) &&
            isEmailCorrect() && passwordRepeatedCorrectly()
        )
            doInBackground {
                viewModel.createUser(
                    email = etEmail.text.toString(),
                    password = etPassword.text.toString()
                )
            }
        else
            Snackbar.make(root, getString(R.string.check_entered_information), Snackbar.LENGTH_LONG).show()
    }

    private fun handleRegistrationResponse(isSuccessful: Boolean) {
        if (isSuccessful) {
            doInBackground {
                viewModel.saveUserToFirestore(
                    email = binding.etEmail.text.toString(),
                    username = binding.etUsername.text.toString()
                )
            }.invokeOnCompletion {
                findNavController().navigate(
                    RegistrationFragmentDirections.actionRegistrationFragmentToTabsFragment()
                )
            }
        } else {
            Snackbar.make(binding.root, getString(R.string.account_already_exists), Snackbar.LENGTH_LONG).show()
        }
    }

    private fun isEmailCorrect(): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(binding.etEmail.text.toString()).matches()
    }

    private fun passwordRepeatedCorrectly(): Boolean {
        return binding.etPassword.text.toString() == binding.etRepeatPassword.text.toString()
    }

}