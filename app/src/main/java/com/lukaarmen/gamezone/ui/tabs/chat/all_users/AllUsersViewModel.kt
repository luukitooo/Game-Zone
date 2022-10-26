package com.lukaarmen.gamezone.ui.tabs.chat.all_users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.lukaarmen.gamezone.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class AllUsersViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @Named("Users") private val usersReference: DatabaseReference
): ViewModel() {

    init {
        viewModelScope.launch {
            getAllUsers()
        }
    }

    private val _usersFlow = MutableStateFlow(emptyList<User>())
    val usersFlow get() = _usersFlow.asStateFlow()

    private val currentUsersList = mutableListOf<User>()

    fun getAllUsers() {
        usersReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java) ?: return
                if (user.uid != firebaseAuth.currentUser!!.uid) {
                    currentUsersList.add(user)
                }
                _usersFlow.value = currentUsersList.toList()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val user = snapshot.getValue(User::class.java) ?: return
                if (user.uid != firebaseAuth.currentUser!!.uid)
                    currentUsersList[currentUsersList.indexOf(currentUsersList.find { it.uid == user.uid })] = user
                _usersFlow.value = currentUsersList.toList()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                return
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                return
            }

            override fun onCancelled(error: DatabaseError) {
                throw error.toException()
            }
        })
    }

}