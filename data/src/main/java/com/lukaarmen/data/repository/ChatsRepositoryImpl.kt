package com.lukaarmen.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.lukaarmen.data.remote.dto.ChatDto
import com.lukaarmen.domain.model.ChatDomain
import com.lukaarmen.domain.repository.firebase.ChatsRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class ChatsRepositoryImpl @Inject constructor(
    @Named("Chats") private val chatsCollection: CollectionReference
) : ChatsRepository {

    override suspend fun addNewChat(chat: ChatDomain) {
        val chats = chatsCollection.get().await().map {
            it.toObject(ChatDto::class.java).toChatDomain()
        }
        chats.forEach { savedChat ->
            if (savedChat.id == chat.id) { return }
        }
        chatsCollection.add(chat).await()
    }

    override suspend fun observeChat(id: String, action: (ChatDomain) -> Unit) {
        chatsCollection
            .whereEqualTo("id", id)
            .addSnapshotListener { value, error ->
                error?.let { exception ->
                    exception.printStackTrace()
                    return@addSnapshotListener
                }

                val user = value?.documents
                    ?.get(0)
                    ?.toObject(ChatDto::class.java)
                    ?.toChatDomain() ?: ChatDomain()

                action.invoke(user)
            }
    }

    override suspend fun setUserTyping(chatId: String, userId: String) {
        val chat = chatsCollection
            .whereEqualTo("id", chatId)
            .get()
            .await()
            .documents[0]

        chat.toObject(ChatDto::class.java)?.toChatDomain()?.typingUserIds?.let { oldTypingUserIds ->
            if (!oldTypingUserIds.contains(userId)) {
                val newTypingList = mutableListOf<String>()
                newTypingList.apply {
                    addAll(oldTypingUserIds)
                    add(userId)
                }
                chatsCollection.document(chat.id).set(
                    mapOf("typingUserIds" to newTypingList),
                    SetOptions.merge()
                )
            }
        }
    }

    override suspend fun removeUserTyping(chatId: String, userId: String) {
        val chat = chatsCollection
            .whereEqualTo("id", chatId)
            .get()
            .await()
            .documents[0]

        chat.toObject(ChatDto::class.java)?.toChatDomain()?.typingUserIds?.let { oldTypingUserIds ->
            if (oldTypingUserIds.contains(userId)) {
                val newTypingList = oldTypingUserIds.toMutableList()
                newTypingList.remove(userId)
                chatsCollection.document(chat.id).set(
                    mapOf("typingUserIds" to newTypingList),
                    SetOptions.merge()
                )
            }
        }
    }

}