package com.lukaarmen.gamezone.common.service.worker.user_activity_status

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.domain.use_case.users.UpdateUserActivityUseCase
import com.lukaarmen.gamezone.common.util.ActivityStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SetStatusToOfflineWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val firebaseAuth: FirebaseAuth,
    private val updateUserActivityUseCase: UpdateUserActivityUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        setStatusToOffline(firebaseAuth.currentUser!!.uid)
        return Result.success()
    }

    private suspend fun setStatusToOffline(uid: String) {
        updateUserActivityUseCase.invoke(
            uid = uid,
            status = ActivityStatus.generateWasActive()
        )

    }

}