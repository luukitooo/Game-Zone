package com.lukaarmen.gamezone.common.service.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.lukaarmen.domain.use_case.matches.GetMatchByIdUseCase
import com.lukaarmen.gamezone.common.util.MessageType
import com.lukaarmen.gamezone.model.Message
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.last
import java.util.*
import javax.inject.Named

@HiltWorker
class ShareLiveWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val getMatchByIdUseCase: GetMatchByIdUseCase,
    private val auth: FirebaseAuth,
    @Named("Messages") private val messagesReference: DatabaseReference
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val matchId = inputData.getInt("matchId", -1)
        val recipientId = inputData.getString("recipientId")
        val match = getMatchByIdUseCase.invoke(matchId).last().success
        if (matchId != -1) {
            messagesReference.push().setValue(
                Message(
                    id = UUID.randomUUID().toString().substring(0, 25),
                    senderId = auth.currentUser!!.uid,
                    recipientId = recipientId,
                    type = MessageType.MATCH.type,
                    text = match?.name ?: "",
                    imageUrl = match?.videoGame?.name ?: "",
                    twitchUrl = match?.streamsList?.get(0)?.rawUrl ?: ""
                )
            )
        }
        return Result.success()
    }

}