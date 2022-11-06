package com.lukaarmen.gamezone.ui.tabs.chat.messages

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.util.GameTitle
import com.lukaarmen.gamezone.common.util.MessageType
import com.lukaarmen.gamezone.databinding.ItemMessageLeftBinding
import com.lukaarmen.gamezone.databinding.ItemMessageLiveLeftBinding
import com.lukaarmen.gamezone.databinding.ItemMessageLiveRightBinding
import com.lukaarmen.gamezone.databinding.ItemMessageRightBinding
import com.lukaarmen.gamezone.model.Message

class MessageAdapter : ListAdapter<Message, RecyclerView.ViewHolder>(MessageItemCallback) {

    var onMatchItemClickListener : ((Message) -> Unit)? = null

    companion object {
        const val TEXT_RIGHT = 1
        const val TEXT_LEFT = 2
        const val LIVE_RIGHT = 3
        const val LIVE_LEFT = 4
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
            TEXT_LEFT -> LeftTextMessageViewHolder(
                ItemMessageLeftBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            LIVE_RIGHT -> RightLiveMessageViewHolder(
                ItemMessageLiveRightBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> LeftLiveMessageViewHolder(
                ItemMessageLiveLeftBinding.inflate(
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
            is RightLiveMessageViewHolder -> holder.bind()
            is LeftLiveMessageViewHolder -> holder.bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        val message = getItem(position)
        if (message.type == MessageType.TEXT.type) {
            return when (message.senderId) {
                FirebaseAuth.getInstance().currentUser!!.uid -> TEXT_RIGHT
                else -> TEXT_LEFT
            }
        } else if (message.type == MessageType.MATCH.type) {
            return when (message.senderId) {
                FirebaseAuth.getInstance().currentUser!!.uid -> LIVE_RIGHT
                else -> LIVE_LEFT
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

    inner class RightLiveMessageViewHolder(private val binding: ItemMessageLiveRightBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(): Unit = with(binding) {
            val message = getItem(adapterPosition)
            ivGame.setImageResource(
                when (message.imageUrl) {
                    GameTitle.CSGO.title -> R.drawable.img_csgo_bg
                    GameTitle.DOTA_2.title -> R.drawable.img_dota2_bg
                    GameTitle.OVERWATCH.title -> R.drawable.img_overwatch_bg
                    else -> R.drawable.imp_rainbow_six_bg
                }
            )
            tvTitle.text = message.text
            root.setOnClickListener {
                onMatchItemClickListener?.invoke(message)
            }
        }
    }

    inner class LeftLiveMessageViewHolder(private val binding: ItemMessageLiveLeftBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(): Unit = with(binding) {
            val message = getItem(adapterPosition)
            ivGame.setImageResource(
                when (message.imageUrl) {
                    GameTitle.CSGO.title -> R.drawable.img_csgo_bg
                    GameTitle.DOTA_2.title -> R.drawable.img_dota2_bg
                    GameTitle.OVERWATCH.title -> R.drawable.img_overwatch_bg
                    else -> R.drawable.imp_rainbow_six_bg
                }
            )
            tvTitle.text = message.text
            root.setOnClickListener {
                onMatchItemClickListener?.invoke(message)
            }
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