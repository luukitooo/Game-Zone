package com.lukaarmen.data.remote.dto

import com.lukaarmen.domain.model.ChatDomain

data class ChatDto(
    val id: String? = null,
    val typingUserIds: List<String>? = null
) {

    fun toChatDomain() = ChatDomain(
        id = id,
        typingUserIds = typingUserIds
    )

}
