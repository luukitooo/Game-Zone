package com.lukaarmen.gamezone.ui.profile

import android.text.method.ScrollingMovementMethod
import androidx.navigation.fragment.findNavController
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.databinding.FragmentAboutUsBinding

class AboutUsFragment : BaseFragment<FragmentAboutUsBinding>(FragmentAboutUsBinding::inflate) {
    override fun init() {
        binding.tvaboutUs.movementMethod = ScrollingMovementMethod()
    }

    override fun listeners() {
        return
    }

    override fun observers() {
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

}