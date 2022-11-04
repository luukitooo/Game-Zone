package com.lukaarmen.gamezone.common.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.domain.usecases.users.UpdateUserActivityUseCase
import com.lukaarmen.gamezone.common.utils.ActivityStatus
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SetStatusToOnlineWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val firebaseAuth: FirebaseAuth,
    private val updateUserActivityUseCase: UpdateUserActivityUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        setStatusToOnline(firebaseAuth.currentUser!!.uid)
        return Result.success()
    }

    private suspend fun setStatusToOnline(uid: String) {
        updateUserActivityUseCase.invoke(
            uid = uid,
            status = ActivityStatus.IS_ACTIVE
        )
    }

}