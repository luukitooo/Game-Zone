package com.lukaarmen.gamezone.ui.auth.newpassword

import androidx.navigation.fragment.findNavController
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.databinding.FragmentNewPasswordBinding

class NewPasswordFragment : BaseFragment<FragmentNewPasswordBinding>(
    FragmentNewPasswordBinding::inflate
) {
    override fun init() {
        return
    }

    override fun listeners() = with(binding) {
        btnConfirmPassword.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun observers() {
        return
    }


}