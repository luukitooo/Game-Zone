package com.lukaarmen.domain.models.firebase

data class ChatDomain(
    val id: String? = null,
    val typingUserIds: List<String>? = null
)
