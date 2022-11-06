package com.lukaarmen.domain.repository.firebase

import com.lukaarmen.domain.model.ChatDomain

interface ChatsRepository {

    suspend fun addNewChat(chat: ChatDomain)

    suspend fun observeChat(id: String, action: (ChatDomain) -> Unit)

    suspend fun setUserTyping(chatId: String, userId: String)

    suspend fun removeUserTyping(chatId: String, userId: String)

}