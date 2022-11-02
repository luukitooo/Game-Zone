package com.lukaarmen.domain.usecases.chats

import com.lukaarmen.domain.models.firebase.ChatDomain
import com.lukaarmen.domain.repositories.firebase.ChatsRepository
import javax.inject.Inject

class CreateChatUseCase @Inject constructor(
    private val repository: ChatsRepository
) {

    suspend operator fun invoke(chat: ChatDomain) {
        repository.addNewChat(chat)
    }

}