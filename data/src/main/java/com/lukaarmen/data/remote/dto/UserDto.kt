package com.lukaarmen.data.remote.dto

import com.lukaarmen.domain.models.firebase.UserDomain

data class UserDto(
    var uid: String? = null,
    var email: String? = null,
    var username: String? = null,
    var imageUrl: String? = null,
    var activity: String? = null,
    var savedUserIds: List<String>? = null
) {

    fun toUserDomain() = UserDomain(
        uid = uid,
        email = email,
        username = username,
        imageUrl = imageUrl,
        activity = activity,
        savedUserIds = savedUserIds
    )

    companion object {

        fun fromDomain(userDomain: UserDomain) = UserDto(
            uid = userDomain.uid,
            email = userDomain.email,
            username = userDomain.username,
            imageUrl = userDomain.imageUrl,
            activity = userDomain.activity,
            savedUserIds = userDomain.savedUserIds
        )

    }

}
