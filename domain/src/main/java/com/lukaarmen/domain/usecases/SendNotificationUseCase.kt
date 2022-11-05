package com.lukaarmen.domain.usecases

import com.lukaarmen.domain.repositories.NotificationRepository
import javax.inject.Inject

class SendNotificationUseCase @Inject constructor(
    private val notificationRepository: NotificationRepository
) {

    suspend operator fun invoke(
        recipientDeviceId: String?,
        currentUserUsername: String?,
        message: String?,
        currentUserImage: String?
    ) = notificationRepository.sendNotification(
        recipientDeviceId = recipientDeviceId,
        currentUserUsername = currentUserUsername,
        message = message,
        currentUserImage = currentUserImage
    )

}