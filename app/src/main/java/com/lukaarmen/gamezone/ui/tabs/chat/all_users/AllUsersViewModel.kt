package com.lukaarmen.gamezone.ui.tabs.chat.all_users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import com.lukaarmen.domain.usecases.users.GetAllUsersObserverUseCase
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
    private val getAllUsersObserverUseCase: GetAllUsersObserverUseCase
): ViewModel() {

    init {
        viewModelScope.launch {
            getAllUsers()
        }
    }

    private val _usersFlow = MutableStateFlow(emptyList<User>())
    val usersFlow get() = _usersFlow.asStateFlow()

    private suspend fun getAllUsers() {
        getAllUsersObserverUseCase.invoke { userDomains ->
            val result = userDomains.map { userDomain ->
                User.fromDomain(userDomain)
            }.filter { user ->
                user.uid != firebaseAuth.currentUser!!.uid
            }
            _usersFlow.value = result
        }
    }

}