package com.lukaarmen.gamezone.ui.tabs.home.homefragment

import android.util.Log
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.findTopNavController
import com.lukaarmen.gamezone.databinding.FragmentHomeBinding

class HomeFragment : BaseFragment<FragmentHomeBinding>(
    FragmentHomeBinding::inflate
) {
    override fun init() {
        Log.d("a", "dds")
    }

    override fun listeners() {

    }

    override fun observers() {
        Log.d("a", "dds")
    }


}