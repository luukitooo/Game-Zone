package com.lukaarmen.domain.repository

interface NotificationRepository {

    suspend fun sendNotification(
        recipientDeviceId: String?,
        currentUserUsername: String?,
        message: String?,
        currentUserImage: String?
    )
}