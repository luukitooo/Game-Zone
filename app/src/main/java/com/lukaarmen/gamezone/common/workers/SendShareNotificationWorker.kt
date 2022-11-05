package com.lukaarmen.gamezone.common.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.data.remote.services.NotificationBody
import com.lukaarmen.data.remote.services.NotificationService
import com.lukaarmen.domain.usecases.GetMatchByIdUseCase
import com.lukaarmen.domain.usecases.users.GetUserByIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SendShareNotificationWorker@AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val sendNotificationService: NotificationService,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val firebaseAuth: FirebaseAuth
): CoroutineWorker(context, params){

    override suspend fun doWork(): Result {
        val userId = inputData.getString("recipientId")
        val recipientUser = getUserByIdUseCase(userId?: "")
        val currentUser = getUserByIdUseCase(firebaseAuth.currentUser!!.uid)
        val notificationBody = NotificationBody(recipientUser.deviceId, NotificationBody.Data(currentUser.username, "Shared match with you", currentUser.imageUrl))

        sendNotificationService.sendNotification(notificationBody)
        return Result.success()
    }

}