package com.lukaarmen.domain.usecases.chats

import com.lukaarmen.domain.repositories.firebase.ChatsRepository
import javax.inject.Inject

class RemoveUserTypingUseCase @Inject constructor(
    private val repository: ChatsRepository
) {

    suspend operator fun invoke(chatId: String, userId: String) {
        repository.removeUserTyping(
            chatId = chatId,
            userId = userId
        )
    }

}