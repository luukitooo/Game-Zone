package com.lukaarmen.gamezone.ui.auth.login

import android.view.View
import androidx.navigation.fragment.findNavController
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.makeLink
import com.lukaarmen.gamezone.databinding.FragmentLoginBinding

class LoginFragment : BaseFragment<FragmentLoginBinding>(
    FragmentLoginBinding::inflate
) {
    override fun init() {
        return
    }

    override fun listeners() = with(binding) {
        btnForgotPassword.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment())
        }
        btnLogin.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToTabsFragment())
        }
        tvCreateAccount.makeLink(
            Pair("Create one", View.OnClickListener {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment())
            })
        )
    }

    override fun observers() {
        return
    }

}