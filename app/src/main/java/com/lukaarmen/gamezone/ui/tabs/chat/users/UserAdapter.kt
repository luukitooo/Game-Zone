package com.lukaarmen.gamezone.ui.tabs.chat.users

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.util.ActivityStatus
import com.lukaarmen.gamezone.databinding.ItemUserBinding
import com.lukaarmen.gamezone.model.User

class UserAdapter : ListAdapter<User, UserAdapter.UserViewHolder>(UserItemCallback) {

    var onItemClickListener: ((User) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = UserViewHolder(
        ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind()
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            val user = getItem(adapterPosition)
            binding.apply {
                tvUsername.text = user.username
                tvActivityStatus.text = user.activity
                activityIndicator.isVisible = user.activity == ActivityStatus.IS_ACTIVE
                seenIndicator.isVisible = user.isMarked
                Glide.with(ivUser)
                    .load(user.imageUrl)
                    .placeholder(R.drawable.img_guest)
                    .into(ivUser)
                root.setOnClickListener {
                    onItemClickListener?.invoke(user)
                }
            }
        }
    }

    private object UserItemCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }

}