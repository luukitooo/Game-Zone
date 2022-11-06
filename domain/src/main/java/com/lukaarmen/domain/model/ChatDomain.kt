package com.lukaarmen.domain.model

data class ChatDomain(
    val id: String? = null,
    val typingUserIds: List<String>? = null
)
