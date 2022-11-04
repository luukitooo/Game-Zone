package com.lukaarmen.gamezone.common.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.domain.usecases.users.UpdateCurrentChatUserIdUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SetCurrentChatUserIdWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val firebaseAuth: FirebaseAuth,
    private val updateCurrentChatUserIdUseCase: UpdateCurrentChatUserIdUseCase
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        setCurrentChatUserId(firebaseAuth.currentUser!!.uid)
        return Result.success()
    }

    private suspend fun setCurrentChatUserId(uId: String){
        val param = inputData.getString("userId")
        updateCurrentChatUserIdUseCase(
            uId,
            param
        )
    }

}