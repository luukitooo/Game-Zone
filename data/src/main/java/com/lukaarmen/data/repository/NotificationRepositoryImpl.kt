package com.lukaarmen.data.repository

import com.lukaarmen.data.remote.dto.NotificationBodyDto
import com.lukaarmen.data.remote.service.NotificationService
import com.lukaarmen.domain.repository.NotificationRepository
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationService: NotificationService
): NotificationRepository {

    override suspend fun sendNotification(
        recipientDeviceId: String?,
        currentUserUsername: String?,
        message: String?,
        currentUserImage: String?
    ) {
        notificationService.sendNotification(
            NotificationBodyDto(recipientDeviceId,
            NotificationBodyDto.DataDto(
                currentUserUsername,
                message,
                currentUserImage
            ))
        )
    }
}