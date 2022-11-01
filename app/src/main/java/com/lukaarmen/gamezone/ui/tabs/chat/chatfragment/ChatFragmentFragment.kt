package com.lukaarmen.gamezone.ui.tabs.chat.chatfragment

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.databinding.FragmentChatBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatFragmentFragment : BaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate) {

    private val viewModel: ChatViewModel by viewModels()

    private val userPagerAdapter = UserPagerAdapter()

    override fun init() {
        binding.viewPager.adapter = userPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, pos ->
            when (pos) {
                0 -> tab.setIcon(R.drawable.ic_friends)
                1 -> tab.setIcon(R.drawable.ic_globe)
            }
        }.attach()
    }

    override fun listeners() {
        userPagerAdapter.onListItemClick = { user ->
            findNavController().navigate(
                ChatFragmentFragmentDirections.actionChatFragmentFragmentToMessagesFragment(
                    recipientId = user.uid ?: "",
                    recipientUsername = user.username ?: "",
                    recipientImageUrl = user.imageUrl ?: ""
                )
            )
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.allUsersFlow.collect { users ->
                userPagerAdapter.setAllUsers(users)
                viewModel.getUsersForCurrentUser()
            }
        }
        doInBackground {
            viewModel.savedUsersFlow.collect { users ->
                userPagerAdapter.setSavedUsers(users)
            }
        }
    }

}