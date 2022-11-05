package com.lukaarmen.gamezone.common.workers

import android.content.Context
import android.util.Log.d
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.domain.usecases.SendNotificationUseCase
import com.lukaarmen.domain.usecases.users.GetUserByIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SendNotificationWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val sendNotificationUseCase: SendNotificationUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val currentUser = inputData.getString("currentUserId")?.let { getUserByIdUseCase(it) }
        val recipientUser = inputData.getString("recipientId")?.let { getUserByIdUseCase(it) }
        val message = inputData.getString("message")

        d("message_log_worker", message.toString())

        return try {
            if (recipientUser?.currentChatUseId != currentUser?.uid) {
                sendNotificationUseCase(
                    recipientDeviceId = recipientUser?.deviceId,
                    currentUserUsername = currentUser?.username,
                    message = message,
                    currentUserImage = currentUser?.imageUrl
                )
            }
            Result.success()
        } catch (e: Throwable) {
            Result.retry()

        }
    }
}