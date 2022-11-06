package com.lukaarmen.gamezone.ui.tabs.chat.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lukaarmen.gamezone.databinding.PageUsersBinding
import com.lukaarmen.gamezone.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class UserPagerAdapter : RecyclerView.Adapter<UserPagerAdapter.PageViewHolder>() {

    var onListItemClick: ((User) -> Unit)? = null

    private val savedUsersFlow = MutableStateFlow(emptyList<User>())
    private val allUsersFlow = MutableStateFlow(emptyList<User>())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PageViewHolder(
        PageUsersBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        when (position) {
            0 -> holder.bindSavedUsersPage()
            1 -> holder.bindAllUsersPage()
        }
    }

    override fun getItemCount(): Int = 2

    fun setSavedUsers(savedUsers: List<User>) {
        savedUsersFlow.value = savedUsers.toList()
    }

    fun setAllUsers(allUsers: List<User>) {
        allUsersFlow.value = allUsers.toList()
    }

    inner class PageViewHolder(private val binding: PageUsersBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bindSavedUsersPage() {
            val userAdapter = UserAdapter()
            binding.root.adapter = userAdapter
            userAdapter.onItemClickListener = { user ->
                onListItemClick?.invoke(user)
            }
            CoroutineScope(Dispatchers.Main).launch {
                savedUsersFlow.collect { users ->
                    userAdapter.submitList(users)
                }
            }
        }
        fun bindAllUsersPage() {
            val userAdapter = UserAdapter()
            binding.root.adapter = userAdapter
            userAdapter.onItemClickListener = { user ->
                onListItemClick?.invoke(user)
            }
            CoroutineScope(Dispatchers.Main).launch {
                allUsersFlow.collect { users ->
                    userAdapter.submitList(users)
                }
            }
        }
    }

}