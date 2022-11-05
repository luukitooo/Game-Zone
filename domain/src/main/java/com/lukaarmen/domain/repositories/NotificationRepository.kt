package com.lukaarmen.domain.repositories

interface NotificationRepository {

    suspend fun sendNotification(
        recipientDeviceId: String?,
        currentUserUsername: String?,
        message: String?,
        currentUserImage: String?
    )
}