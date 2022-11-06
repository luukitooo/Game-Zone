package com.lukaarmen.domain.model

data class UserDomain(
    var uid: String? = null,
    var email: String? = null,
    var username: String? = null,
    var imageUrl: String? = null,
    var activity: String? = null,
    var savedUserIds: List<String>? = null,
    var deviceId: String? = null,
    var currentChatUseId: String? = null,
    var markedUsers: List<String>? = null
)