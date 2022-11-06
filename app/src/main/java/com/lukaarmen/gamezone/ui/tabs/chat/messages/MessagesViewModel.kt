package com.lukaarmen.gamezone.ui.tabs.chat.messages

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.lukaarmen.data.remote.service.NotificationService
import com.lukaarmen.domain.model.ChatDomain
import com.lukaarmen.domain.use_case.chats.CreateChatUseCase
import com.lukaarmen.domain.use_case.chats.ObserveChatUseCase
import com.lukaarmen.domain.use_case.chats.RemoveUserTypingUseCase
import com.lukaarmen.domain.use_case.chats.SetUserTypingUseCase
import com.lukaarmen.domain.use_case.users.GetUserByIdUseCase
import com.lukaarmen.domain.use_case.users.RemoveUserMarkedUseCase
import com.lukaarmen.domain.use_case.users.SaveUserIdUseCase
import com.lukaarmen.domain.use_case.users.SetUserMarkedUseCase
import com.lukaarmen.gamezone.common.util.MessageType
import com.lukaarmen.gamezone.model.Chat
import com.lukaarmen.gamezone.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MessagesViewModel @Inject constructor(
    @Named("Messages") private val messagesReference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth,
    private val saveUserIdUseCase: SaveUserIdUseCase,
    private val setUserTypingUseCase: SetUserTypingUseCase,
    private val removeUserTypingUseCase: RemoveUserTypingUseCase,
    private val createChatUseCase: CreateChatUseCase,
    private val observeChatUseCase: ObserveChatUseCase,
    private val savedStateHandler: SavedStateHandle,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val sendNotificationService: NotificationService,
    private val setUserMarkedUseCase: SetUserMarkedUseCase,
    private val removeUserMarkedUseCase: RemoveUserMarkedUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            saveUserIdUseCase.invoke(
                selfId = firebaseAuth.currentUser!!.uid,
                otherUserId = savedStateHandler.get<String>("recipientId") ?: return@launch,
            )
        }
        viewModelScope.launch {
            saveUserIdUseCase.invoke(
                selfId = savedStateHandler.get<String>("recipientId") ?: return@launch,
                otherUserId = firebaseAuth.currentUser!!.uid,
            )
        }
    }

    private val _messagesFlow = MutableStateFlow(emptyList<Message>())
    val messagesFlow get() = _messagesFlow.asStateFlow()

    private val currentMessagesList = mutableListOf<Message>()

    fun getMessages(recipientId: String) {
        messagesReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(Message::class.java) ?: return
                val sentFromMe = message.senderId == firebaseAuth.currentUser!!.uid && message.recipientId == recipientId
                val sentToMe = message.senderId == recipientId && message.recipientId == firebaseAuth.currentUser!!.uid
                if (sentFromMe || sentToMe) {
                    currentMessagesList.add(message)
                }
                _messagesFlow.value = currentMessagesList.toList()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                return
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                return
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                return
            }

            override fun onCancelled(error: DatabaseError) {
                return
            }
        })
    }

    fun sendTextMessage(recipientId: String, message: String) {
        messagesReference.push().setValue(
            Message(
                id = UUID.randomUUID().toString().substring(0, 25),
                senderId = firebaseAuth.currentUser!!.uid,
                recipientId = recipientId,
                type = MessageType.TEXT.type,
                text = message
            )
        )
    }

    suspend fun createUser() {
        createChatUseCase.invoke(
            ChatDomain(
                id = savedStateHandler
                    .get<String>("recipientId")
                    .plus(firebaseAuth.currentUser!!.uid)
                    .toSortedSet()
                    .joinToString(""),
                typingUserIds = emptyList()
            )
        )
    }

    suspend fun setCurrentUserTyping() {
        setUserTypingUseCase.invoke(
            chatId = savedStateHandler
                .get<String>("recipientId")
                .plus(firebaseAuth.currentUser!!.uid)
                .toSortedSet()
                .joinToString(""),
            userId = firebaseAuth.currentUser!!.uid
        )
    }

    suspend fun removeCurrentUserTyping() {
        removeUserTypingUseCase.invoke(
            chatId = savedStateHandler
                .get<String>("recipientId")
                .plus(firebaseAuth.currentUser!!.uid)
                .toSortedSet()
                .joinToString(""),
            userId = firebaseAuth.currentUser!!.uid
        )
    }

    suspend fun observeCurrentChat(action: (Chat) -> Unit) {
        observeChatUseCase.invoke(
            id = savedStateHandler
                .get<String>("recipientId")
                .plus(firebaseAuth.currentUser!!.uid)
                .toSortedSet()
                .joinToString(""),
        ) { chatDomain ->
            action.invoke(Chat.fromChatDomain(chatDomain))
        }
    }

    suspend fun setUserSeen(selfId: String, otherId: String) {
        setUserMarkedUseCase.invoke(selfId, otherId)
    }

    suspend fun removeUserSeen(selfId: String, otherId: String) {
        removeUserMarkedUseCase.invoke(selfId, otherId)
    }

}