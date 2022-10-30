package com

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log.d
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.lukaarmen.domain.models.MatchDomain
import com.lukaarmen.domain.usecases.GetAllRunningMatchesUseCase
import com.lukaarmen.gamezone.DataStore
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.common.extentions.getStreamPreview
import com.lukaarmen.gamezone.common.utils.Quality
import com.lukaarmen.gamezone.ui.MainActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import java.net.URL
import kotlin.random.Random

@HiltWorker
class LivesWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted val workerParameters: WorkerParameters,
    private val getAllRunningMatchesUseCase: GetAllRunningMatchesUseCase,
    private val dataStore: DataStore
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result {
        return try {
            getAllRunningMatchesUseCase(null).collect { result ->
                result.onSuccess { matchesList ->
                    if (matchesList.isNotEmpty()) {
                        checkForNewLives(matchesList)
                        dataStore.save(stringPreferencesKey("LATEST_LIVE"), matchesList.first().name!!)
                    }
                }
            }
            Result.success()
        } catch (e: Throwable) {
            d("livesWorker_error", e.message.toString())
            Result.retry()
        }
    }

    private suspend fun checkForNewLives(livesList: List<MatchDomain>) {
        //sendNotification(livesList.first())
        val saved = dataStore.getPreferences(stringPreferencesKey("LATEST_LIVE")).first()

        d("livesWorker_dataStore_collector", saved)
        livesList.forEach { match ->
            if(saved != match.name && saved != "") sendNotification(match)
            else return
        }
    }


    private suspend fun sendNotification(match: MatchDomain) {

        val intent = Intent(appContext, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra("is_liveNotification", true)
            putExtra("gameId", "match.id.toString()")
        }

        val pendingIntent = PendingIntent.getActivity(
            appContext, 0, intent, PendingIntent.FLAG_IMMUTABLE
        )

        var image: Bitmap? = null
        try {
            val url = URL(match.streamsList?.last()?.embedUrl.toString().getStreamPreview(Quality.LOW))
            image = BitmapFactory.decodeStream(withContext(Dispatchers.IO) {
                url.openConnection().getInputStream()
            })
        }catch (e: Throwable){
            d("livesWorker_bitmap_error", e.message.toString())
        }
        val builder = NotificationCompat
            .Builder(appContext, "lives_channel")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("New live started")
            .setContentText(match.name)
            .setLargeIcon(image)
            .setStyle(NotificationCompat.BigPictureStyle().bigPicture(image).bigLargeIcon(null))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        with(NotificationManagerCompat.from(appContext)) {
            notify(Random.nextInt(), builder.build())
        }
    }
}