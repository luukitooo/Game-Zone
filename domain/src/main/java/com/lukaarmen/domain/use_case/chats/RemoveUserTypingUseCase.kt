package com.lukaarmen.domain.use_case.chats

import com.lukaarmen.domain.repository.firebase.ChatsRepository
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