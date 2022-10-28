package com.lukaarmen.gamezone.ui.tabs.chat.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.gamezone.common.utils.MessageTypes
import com.lukaarmen.gamezone.databinding.ItemMessageLeftBinding
import com.lukaarmen.gamezone.databinding.ItemMessageRightBinding
import com.lukaarmen.gamezone.model.Message
import javax.inject.Inject

class MessageAdapter : ListAdapter<Message, RecyclerView.ViewHolder>(MessageItemCallback) {

    companion object {
        const val TEXT_RIGHT = 1
        const val TEXT_LEFT = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TEXT_RIGHT -> RightTextMessageViewHolder(
                ItemMessageRightBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> LeftTextMessageViewHolder(
                ItemMessageLeftBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is RightTextMessageViewHolder -> holder.bind()
            is LeftTextMessageViewHolder -> holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        if (message.type == MessageTypes.TEXT.type) {
            return when (message.senderId) {
                FirebaseAuth.getInstance().currentUser!!.uid -> TEXT_RIGHT
                else -> TEXT_LEFT
            }
        }
        return -1
    }

    inner class RightTextMessageViewHolder(private val binding: ItemMessageRightBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(): Unit = with(binding) {
            val message = getItem(adapterPosition)
            tvMessage.text = message.text
        }
    }

    inner class LeftTextMessageViewHolder(private val binding: ItemMessageLeftBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(): Unit = with(binding) {
            val message = getItem(adapterPosition)
            tvMessage.text = message.text
        }
    }

    private object MessageItemCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
            return oldItem == newItem
        }
    }

}