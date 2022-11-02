package com.lukaarmen.gamezone.ui.tabs.chat.messages

import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.hide
import com.lukaarmen.gamezone.common.extentions.show
import com.lukaarmen.gamezone.databinding.FragmentMessagesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import pl.droidsonroids.gif.GifImageView
import javax.inject.Inject

@AndroidEntryPoint
class MessagesFragment : BaseFragment<FragmentMessagesBinding>(FragmentMessagesBinding::inflate) {

    @Inject
    lateinit var auth: FirebaseAuth

    private val viewModel: MessagesViewModel by viewModels()

    private val args: MessagesFragmentArgs by navArgs()

    private val messageAdapter = MessageAdapter()

    private var typingJob: Job? = null

    override fun init(): Unit = with(binding) {
        tvUsername.text = args.recipientUsername
        Glide.with(ivUser)
            .load(args.recipientImageUrl)
            .placeholder(R.drawable.img_guest)
            .into(ivUser)
        rvMessages.adapter = messageAdapter
        viewModel.getMessages(args.recipientId)
        doInBackground {
            viewModel.createUser()
        }.invokeOnCompletion {
            doInBackground {
                handleChatState(binding.gifIsTyping)
            }
        }
    }

    override fun listeners(): Unit = with(binding) {
        btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
        btnSend.setOnClickListener {
            handleMessageSending(message = etMessage.text.toString())
            etMessage.text?.clear()
        }
        etMessage.doOnTextChanged { text, start, before, count ->
            if (count > 0)
                doInBackground { viewModel.setCurrentUserTyping() }
            typingJob?.cancel()
            typingJob = null
            typingJob = doInBackground {
                delay(1000)
                viewModel.removeCurrentUserTyping()
            }
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

    private suspend fun handleChatState(loader: GifImageView) {
        viewModel.observeCurrentChat { chat ->
            if (chat.typingUserIds?.contains(args.recipientId) == true) {
                loader.show()
            } else {
                loader.hide()
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