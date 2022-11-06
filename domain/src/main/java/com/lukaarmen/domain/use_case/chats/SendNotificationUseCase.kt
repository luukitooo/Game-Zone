package com.lukaarmen.domain.use_case.chats

import com.lukaarmen.domain.repository.NotificationRepository
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