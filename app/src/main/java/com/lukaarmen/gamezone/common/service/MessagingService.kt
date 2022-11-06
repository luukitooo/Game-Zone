package com.lukaarmen.gamezone.common.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.core.app.NotificationCompat
import com.bumptech.glide.Glide
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.ui.MainActivity
import java.util.*

class MessagingService : FirebaseMessagingService() {

    private val NOTIFICATION_CHANEL = "NOTIFICATION_CHANEL"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
       super.onMessageReceived(message)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random().nextInt(3000)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager)
        }
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANEL)
            .setSmallIcon(R.drawable.ic_gamepad)
            .setContentTitle(message.data["title"])
            .setContentText(message.data["message"])
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .setLargeIcon(getProfilePhoto(message.data["sender_image"])).setStyle(NotificationCompat.BigPictureStyle())

        notificationManager.notify(notificationID, notificationBuilder.build())
    }

    private fun setupChannels(notificationManager: NotificationManager?) {
        val chatChannelName = "Chat notification"
        val chatChannelDescription = "Device to device notification"

        val chatChannel = NotificationChannel(
            NOTIFICATION_CHANEL,
            chatChannelName,
            NotificationManager.IMPORTANCE_HIGH
        )
        chatChannel.description = chatChannelDescription
        notificationManager?.createNotificationChannel(chatChannel)
    }

    private fun getProfilePhoto(url: String?): Bitmap? {
        return if(!url.isNullOrEmpty()) Glide.with(this)
            .asBitmap()
            .load(url)
            .circleCrop()
            .submit()
            .get()
        else Glide.with(this)
            .asBitmap()
            .load(R.drawable.img_guest)
            .circleCrop()
            .submit()
            .get()
    }

}