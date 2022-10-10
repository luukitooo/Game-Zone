package com.lukaarmen.gamezone.ui.auth.forgotpassword

import androidx.navigation.fragment.findNavController
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.databinding.FragmentForgotPasswordBinding

class ForgotPasswordFragment : BaseFragment<FragmentForgotPasswordBinding>(
    FragmentForgotPasswordBinding::inflate
) {
    override fun init() {
        return
    }

    override fun listeners() = with(binding) {
        btnSendLink.setOnClickListener {
            findNavController().navigate(ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToNewPasswordFragment())
        }
    }

    override fun observers() {
        return
    }


}