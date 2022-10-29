package com.lukaarmen.gamezone.model

import com.lukaarmen.domain.models.firebase.UserDomain

data class User(
    var uid: String? = null,
    var email: String? = null,
    var username: String? = null,
    var imageUrl: String? = null,
) {

    fun toDomain() = UserDomain(
        uid = uid,
        email = email,
        username = username,
        imageUrl = imageUrl,
    )

    companion object {

        fun fromDomain(userDomain: UserDomain) = User(
            uid = userDomain.uid,
            email = userDomain.email,
            username = userDomain.username,
            imageUrl = userDomain.imageUrl,
        )

    }

}