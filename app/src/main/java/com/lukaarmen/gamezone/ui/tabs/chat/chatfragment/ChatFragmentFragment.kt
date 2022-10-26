package com.lukaarmen.gamezone.ui.tabs.chat.chatfragment

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragmentFragment : BaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate) {

    override fun init() {
        initViewPager()
    }

    override fun listeners() {
        return
    }

    override fun observers() {
        return
    }

    private fun initViewPager() {
        binding.viewPager.adapter = ChatFragmentStateAdapter(requireActivity() as AppCompatActivity)
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.setIcon(R.drawable.ic_friends)
                1 -> tab.setIcon(R.drawable.ic_globe)
            }
        }.attach()
    }

}