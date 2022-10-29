package com.lukaarmen.domain.models.firebase

data class UserDomain(
    var uid: String? = null,
    var email: String? = null,
    var username: String? = null,
    var imageUrl: String? = null,
)