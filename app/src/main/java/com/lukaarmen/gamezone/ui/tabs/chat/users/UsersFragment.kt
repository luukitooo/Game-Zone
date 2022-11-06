package com.lukaarmen.gamezone.ui.tabs.chat.users

import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extension.doInBackground
import com.lukaarmen.gamezone.common.extension.hide
import com.lukaarmen.gamezone.common.extension.show
import com.lukaarmen.gamezone.databinding.FragmentChatBinding
import com.lukaarmen.gamezone.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UsersFragment : BaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate) {

    private val viewModel: UsersViewModel by viewModels()

    private val userPagerAdapter = UserPagerAdapter()

    private var isSearching = false
    private var currentAllUsersList = emptyList<User>()
    private var currentSavedUsersList = emptyList<User>()

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
                UsersFragmentDirections.actionChatFragmentFragmentToMessagesFragment(
                    recipientId = user.uid ?: "",
                    recipientUsername = user.username ?: "",
                    recipientImageUrl = user.imageUrl ?: ""
                )
            )
        }
        binding.btnSearch.setOnClickListener {
            isSearching = !isSearching
            toggleSearch(isSearching)
        }
        binding.etSearch.doOnTextChanged { text, start, before, _ ->
            if (start != 0 || before != 0) {
                searchForUsers(text.toString())
            }
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.allUsersFlow.collect { users ->
                userPagerAdapter.setAllUsers(users)
                currentAllUsersList = users
            }
        }
        doInBackground {
            viewModel.savedUsersFlow.collect { users ->
                userPagerAdapter.setSavedUsers(users)
                currentSavedUsersList = users
            }
        }
    }

    private fun toggleSearch(isSearching: Boolean) {
        if (isSearching) {
            binding.btnSearch.setImageResource(R.drawable.ic_cross)
            binding.etSearch.show()
        } else {
            binding.btnSearch.setImageResource(R.drawable.ic_search)
            binding.etSearch.hide()
        }
    }

    private fun searchForUsers(username: String) {
        userPagerAdapter.setAllUsers(
            currentAllUsersList.filter { user ->
                user.username?.contains(username) ?: false
            }
        )
        userPagerAdapter.setSavedUsers(
            currentSavedUsersList.filter { user ->
                user.username?.contains(username) ?: false
            }
        )
    }

}