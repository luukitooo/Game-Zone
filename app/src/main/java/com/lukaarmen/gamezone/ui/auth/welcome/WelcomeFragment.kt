package com.lukaarmen.gamezone.ui.auth.welcome

import androidx.navigation.fragment.findNavController
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.databinding.FragmentWelcomeBinding

class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(
    FragmentWelcomeBinding::inflate
) {
    override fun init() {
        // TODO: AuthenticationManager.isLoggedIn() instead of static boolean
        checkSession(false)
    }

    override fun listeners() = with(binding) {
        btnNext.setOnClickListener {
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
        }
    }

    override fun observers() {
        return
    }

    private fun checkSession(isLoggedIn: Boolean){
        if(isLoggedIn){
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToTabsFragment())
        }
    }

}