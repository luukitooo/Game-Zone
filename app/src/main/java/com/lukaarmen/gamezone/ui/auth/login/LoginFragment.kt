package com.lukaarmen.gamezone.ui.auth.login

import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.areLinesEmpty
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.makeLink
import com.lukaarmen.gamezone.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
) {

    private val viewModel: LoginViewModel by viewModels()

    override fun init() {
        return
    }

    override fun listeners() = with(binding) {
        btnForgotPassword.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
        }
        btnLogin.setOnClickListener {
            handleLoginRequest()
        }
        tvCreateAccount.makeLink(
            Pair("Create one", View.OnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
            })
        )
    }

    override fun observers() {
        doInBackground {
            viewModel.loginSuccessFlow.collect { isSuccessful ->
                handleLoginResponse(isSuccessful)
            }
        }
    }

    private fun handleLoginRequest() = with(binding) {
        if (!areLinesEmpty(etEmail, etPassword)){
            doInBackground {
                viewModel.loginWith(
                    binding.etEmail.text.toString(),
                    binding.etPassword.text.toString()
                )
            }
        } else {
            Snackbar.make(root, "Lines are empty...", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun handleLoginResponse(isSuccessful: Boolean) {
        if (isSuccessful) {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToTabsFragment()
            )
        } else {
            Snackbar.make(binding.root, "Invalid user...", Snackbar.LENGTH_LONG).show()
        }
    }

}