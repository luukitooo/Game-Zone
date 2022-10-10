package com.lukaarmen.gamezone.ui.auth.registration

import androidx.navigation.fragment.findNavController
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.databinding.FragmentRegistrationBinding

class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>(
    FragmentRegistrationBinding::inflate
) {
    override fun init() {
        return
    }

    override fun listeners() = with(binding) {
        btnCreate.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun observers() {
        return
    }

}