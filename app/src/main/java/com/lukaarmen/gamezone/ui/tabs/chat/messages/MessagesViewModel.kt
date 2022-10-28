package com.lukaarmen.gamezone.ui.tabs.chat.messages

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.lukaarmen.gamezone.common.utils.MessageTypes
import com.lukaarmen.gamezone.model.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.crypto.KeyGenerator
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MessagesViewModel @Inject constructor(
    @Named("Messages") private val messagesReference: DatabaseReference,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

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
                type = MessageTypes.TEXT.type,
                text = message
            )
        )
    }

}