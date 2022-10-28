package com.lukaarmen.gamezone.ui.tabs.chat.chatfragment

import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lukaarmen.gamezone.model.User
import com.lukaarmen.gamezone.ui.tabs.chat.all_users.AllUsersFragment
import com.lukaarmen.gamezone.ui.tabs.chat.saved_users.SavedUsersFragment

class ChatFragmentStateAdapter(
    activity: AppCompatActivity
) : FragmentStateAdapter(activity) {

    var onUserItemClick: ((User) -> Unit)? = null

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SavedUsersFragment()
            else -> AllUsersFragment(
                onUserItemClick = { user ->
                    onUserItemClick?.invoke(user)
                }
            )
        }
    }
}