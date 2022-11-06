package com.lukaarmen.domain.use_case.chats

import com.lukaarmen.domain.model.ChatDomain
import com.lukaarmen.domain.repository.firebase.ChatsRepository
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