package com.lukaarmen.gamezone.ui.tabs.tabsfragment

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.databinding.FragmentTabsBinding

class TabsFragment : BaseFragment<FragmentTabsBinding>(
    FragmentTabsBinding::inflate
) {

    private var backPressedTime: Long = 0

    override fun init() {
        initNavigation()
        onBackPressed()
    }

    override fun listeners() {
        return
    }

    override fun observers() {
        return
    }

    private fun initNavigation() {
        val navHost = childFragmentManager.findFragmentById(R.id.tabsContainer) as NavHostFragment
        val navController = navHost.navController
        NavigationUI.setupWithNavController(binding.bottomNavigation, navController)
    }

    private fun onBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 3000 > System.currentTimeMillis()) {
                    activity!!.finish()
                } else {
                    Toast.makeText(requireContext(), R.string.double_backpress, Toast.LENGTH_LONG)
                        .show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        })
    }

}