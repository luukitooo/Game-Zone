package com.lukaarmen.data.remote.dto

import com.lukaarmen.domain.model.UserDomain

data class UserDto(
    var uid: String? = null,
    var email: String? = null,
    var username: String? = null,
    var imageUrl: String? = null,
    var activity: String? = null,
    var savedUserIds: List<String>? = null,
    var deviceId: String? = null,
    var currentChatUseId: String? = null,
    var markedUsers: List<String>? = null
) {

    fun toUserDomain() = UserDomain(
        uid = uid,
        email = email,
        username = username,
        imageUrl = imageUrl,
        activity = activity,
        savedUserIds = savedUserIds,
        deviceId = deviceId,
        currentChatUseId = currentChatUseId,
        markedUsers = markedUsers
    )

    companion object {

        fun fromDomain(userDomain: UserDomain) = UserDto(
            uid = userDomain.uid,
            email = userDomain.email,
            username = userDomain.username,
            imageUrl = userDomain.imageUrl,
            activity = userDomain.activity,
            savedUserIds = userDomain.savedUserIds,
            deviceId = userDomain.deviceId,
            currentChatUseId = userDomain.currentChatUseId,
            markedUsers = userDomain.markedUsers
        )

    }

}
