package com.lukaarmen.gamezone.ui.tabs.home.livematchdetails.share

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.work.*
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.gamezone.common.extentions.doInBackground
import com.lukaarmen.gamezone.common.extentions.hide
import com.lukaarmen.gamezone.common.workers.SendNotificationWorker
import com.lukaarmen.gamezone.common.workers.ShareLiveWorker
import com.lukaarmen.gamezone.databinding.BottomSheedShareBinding
import com.lukaarmen.gamezone.model.User
import com.lukaarmen.gamezone.ui.tabs.chat.chatfragment.UserAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ShareBottomSheet : BottomSheetDialogFragment() {

    @Inject
    lateinit var auth: FirebaseAuth

    private var _binding: BottomSheedShareBinding? = null
    val binding get() = _binding!!

    private val viewModel: ShareViewModel by viewModels()

    private val args: ShareBottomSheetArgs by navArgs()

    private val userAdapter = UserAdapter()

    private var fullUserList = emptyList<User>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheedShareBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

        listeners()

        observers()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun init() {
        binding.rvUsers.adapter = userAdapter
    }

    private fun listeners() {
        binding.etSearch.doAfterTextChanged { editable ->
            userAdapter.submitList(
                fullUserList.filter { user ->
                    user.username?.contains(editable.toString()) ?: false
                }
            )
        }
        userAdapter.onItemClickListener = { user ->
            startShareWorker(
                matchId = args.matchId,
                recipientId = user.uid ?: ""
            )
            startNotificationWorker(
                currentUserId = auth.currentUser!!.uid,
                recipientId = user.uid ?: "",
                message = "Shared match with you"
            )
            dismiss()
        }
    }

    private fun observers() {
        doInBackground {
            viewModel.usersFlow.collect { users ->
                userAdapter.submitList(users)
                fullUserList = users
                binding.progressBar.hide()
            }
        }
    }

    private fun startShareWorker(matchId: Int, recipientId: String) {
        val shareWork = OneTimeWorkRequest.Builder(ShareLiveWorker::class.java)
        val data = Data.Builder()
            .putInt("matchId", matchId)
            .putString("recipientId", recipientId)
            .build()
        shareWork.setInputData(data)
        WorkManager.getInstance(requireContext()).enqueue(shareWork.build())
    }

    private fun startNotificationWorker(currentUserId: String, recipientId: String, message: String){
        val data = workDataOf(
            "currentUserId" to currentUserId,
            "recipientId" to recipientId,
            "message" to message
        )
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()

        val work = OneTimeWorkRequestBuilder<SendNotificationWorker>()
            .setConstraints(constraints)
            .setInputData(data)
            .build()

        WorkManager.getInstance(requireContext()).enqueue(work)
    }
}