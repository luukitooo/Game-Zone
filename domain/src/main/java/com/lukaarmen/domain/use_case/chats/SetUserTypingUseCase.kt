package com.lukaarmen.domain.use_case.chats

import com.lukaarmen.domain.repository.firebase.ChatsRepository
import javax.inject.Inject

class SetUserTypingUseCase @Inject constructor(
    private val repository: ChatsRepository
) {

    suspend operator fun invoke(chatId: String, userId: String) {
        repository.setUserTyping(
            chatId = chatId,
            userId = userId
        )
    }

}