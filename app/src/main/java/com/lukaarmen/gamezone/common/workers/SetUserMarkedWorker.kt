package com.lukaarmen.gamezone.common.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lukaarmen.domain.usecases.users.SetUserMarkedUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SetUserMarkedWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    private val setUserMarkedUseCase: SetUserMarkedUseCase
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val selfId = inputData.getString("selfId") ?: return Result.failure()
        val otherId = inputData.getString("otherId") ?: return Result.failure()
        setUserMarkedUseCase.invoke(selfId, otherId)
        return Result.success()
    }

}