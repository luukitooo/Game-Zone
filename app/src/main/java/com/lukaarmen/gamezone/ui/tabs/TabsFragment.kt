package com.lukaarmen.gamezone.ui.tabs

import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extension.doInBackground
import com.lukaarmen.gamezone.common.service.worker.user_activity_status.SetStatusToOfflineWorker
import com.lukaarmen.gamezone.common.service.worker.user_activity_status.SetStatusToOnlineWorker
import com.lukaarmen.gamezone.databinding.FragmentTabsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TabsFragment : BaseFragment<FragmentTabsBinding>(
    FragmentTabsBinding::inflate
) {

    private val viewModel by viewModels<TabsViewModel>()

    private var backPressedTime: Long = 0

    override fun init() {
        initNavigation()
        onBackPressed()
        doInBackground {
            viewModel.setToken()
        }
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

    override fun onStart() {
        super.onStart()
        OneTimeWorkRequestBuilder<SetStatusToOnlineWorker>().build().also {
            WorkManager.getInstance(requireContext()).enqueue(it)
        }
    }

    override fun onStop() {
        super.onStop()
        OneTimeWorkRequestBuilder<SetStatusToOfflineWorker>().build().also {
            WorkManager.getInstance(requireContext()).enqueue(it)
        }
    }

}