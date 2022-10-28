package com.lukaarmen.gamezone.ui.tabs.chat.messages

import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.databinding.FragmentMessagesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.yield

@AndroidEntryPoint
class MessagesFragment : BaseFragment<FragmentMessagesBinding>(FragmentMessagesBinding::inflate) {

    private val viewModel: MessagesViewModel by viewModels()

    private val args: MessagesFragmentArgs by navArgs()

    private val messageAdapter = MessageAdapter()

    override fun init(): Unit = with(binding) {
        tvUsername.text = args.recipientUsername
        Glide.with(ivUser)
            .load(args.recipientImageUrl)
            .placeholder(R.drawable.img_guest)
            .into(ivUser)
        rvMessages.adapter = messageAdapter
        viewModel.getMessages(args.recipientId)
    }

    override fun listeners(): Unit = with(binding) {
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        btnSend.setOnClickListener {
            handleMessageSending(message = etMessage.text.toString())
            etMessage.text?.clear()
        }
    }

    override fun observers() {
        doInBackground {
            viewModel.messagesFlow.collect { messages ->
                messageAdapter.submitList(messages)
                scrollRecyclerToBottom()
            }
        }
    }

    private fun handleMessageSending(message: String) {
        if (message.isNotEmpty()) {
            viewModel.sendTextMessage(
                recipientId = args.recipientId,
                message = message
            )
        }
    }

    private fun scrollRecyclerToBottom() {
        try {
            binding.rvMessages.smoothScrollToPosition(
                messageAdapter.currentList.size
            )
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

}