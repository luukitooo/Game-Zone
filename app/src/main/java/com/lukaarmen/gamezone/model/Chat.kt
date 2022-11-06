package com.lukaarmen.gamezone.model

import com.lukaarmen.domain.model.ChatDomain

data class Chat(
    val id: String? = null,
    val typingUserIds: List<String>? = null
) {

    companion object {
        fun fromChatDomain(chatDomain: ChatDomain) = Chat(
            id = chatDomain.id,
            typingUserIds = chatDomain.typingUserIds
        )
    }

}
