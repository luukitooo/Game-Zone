package com.lukaarmen.gamezone.ui.auth.welcome

import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.databinding.FragmentWelcomeBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WelcomeFragment : BaseFragment<FragmentWelcomeBinding>(
    FragmentWelcomeBinding::inflate
) {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun init() {
        firebaseAuth.currentUser?.let {
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToTabsFragment())
        }
    }

    override fun listeners() = with(binding) {
        btnNext.setOnClickListener {
            findNavController().navigate(WelcomeFragmentDirections.actionWelcomeFragmentToLoginFragment())
        }
    }

    override fun observers() {
        return
    }
}