package com.lukaarmen.domain.usecases.chats

import com.lukaarmen.domain.models.firebase.ChatDomain
import com.lukaarmen.domain.repositories.firebase.ChatsRepository
import javax.inject.Inject

class ObserveChatUseCase @Inject constructor(
    private val repository: ChatsRepository
) {

    suspend operator fun invoke(id: String, action: (ChatDomain) -> Unit) {
        repository.observeChat(id) { user ->
            action.invoke(user)
        }
    }

}