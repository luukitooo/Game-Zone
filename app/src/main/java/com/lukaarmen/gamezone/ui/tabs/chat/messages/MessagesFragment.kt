package com.lukaarmen.gamezone.ui.tabs.chat.messages

import android.content.Intent
import android.net.Uri
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.work.*
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.messaging.ktx.remoteMessage
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.base.BaseFragment
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.hide
import com.lukaarmen.gamezone.common.extentions.show
import com.lukaarmen.gamezone.common.workers.SendNotificationWorker
import com.lukaarmen.gamezone.common.workers.SetCurrentChatUserIdWorker
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

    private val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

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
        messageAdapter.onMatchItemClickListener = { message ->
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(message.twitchUrl)
            startActivity(intent)
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
            startNotificationWorker(
                currentUserId = auth.currentUser!!.uid,
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

    private fun startNotificationWorker(currentUserId: String, recipientId: String, message: String){
        val data = workDataOf(
            "currentUserId" to currentUserId,
            "recipientId" to recipientId,
            "message" to message
        )

        val work = OneTimeWorkRequestBuilder<SendNotificationWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(work)
    }

    override fun onStart() {
        super.onStart()
        val data = workDataOf("userId" to args.recipientId)
        val work = OneTimeWorkRequestBuilder<SetCurrentChatUserIdWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(work)
    }

    override fun onPause() {
        super.onPause()
        val data = workDataOf("userId" to "")
        val work = OneTimeWorkRequestBuilder<SetCurrentChatUserIdWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(work)
    }

}