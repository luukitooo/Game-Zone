package com.lukaarmen.domain.use_case.chats

import com.lukaarmen.domain.model.ChatDomain
import com.lukaarmen.domain.repository.firebase.ChatsRepository
import javax.inject.Inject

class CreateChatUseCase @Inject constructor(
    private val repository: ChatsRepository
) {

    suspend operator fun invoke(chat: ChatDomain) {
        repository.addNewChat(chat)
    }

}