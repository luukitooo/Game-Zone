package com.lukaarmen.gamezone.ui.tabs.chat.all_users

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.findTopNavController
import com.lukaarmen.gamezone.databinding.FragmentAllUsersBinding
import com.lukaarmen.gamezone.model.User
import com.lukaarmen.gamezone.ui.tabs.chat.UserAdapter
import com.lukaarmen.gamezone.ui.tabs.chat.chatfragment.ChatFragmentFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AllUsersFragment(
    var onUserItemClick: (User) -> Unit = {}
) : BaseFragment<FragmentAllUsersBinding>(FragmentAllUsersBinding::inflate) {

    private val viewModel: AllUsersViewModel by viewModels()

    private val userAdapter = UserAdapter()

    override fun init() {
        binding.rvUsers.adapter = userAdapter
    }

    override fun listeners() {
        doInBackground {
            viewModel.usersFlow.collect { usersList ->
                userAdapter.submitList(usersList)
            }
        }
        userAdapter.onItemClickListener = { user ->
            onUserItemClick.invoke(user)
        }
    }

    override fun observers() {
        return
    }

}