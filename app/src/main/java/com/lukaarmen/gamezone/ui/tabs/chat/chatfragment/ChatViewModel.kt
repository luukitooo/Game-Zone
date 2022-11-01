package com.lukaarmen.gamezone.ui.tabs.chat.chatfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.domain.usecases.users.GetAllUsersObserverUseCase
import com.lukaarmen.domain.usecases.users.GetUsersForUserUseCase
import com.lukaarmen.gamezone.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val getAllUsersObserverUseCase: GetAllUsersObserverUseCase,
    private val getUsersForUserUseCase: GetUsersForUserUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            setAllUsersObserver()
            getUsersForCurrentUser()
        }
    }

    private val _allUsersFlow = MutableStateFlow(emptyList<User>())
    val allUsersFlow get() = _allUsersFlow.asStateFlow()

    private val _savedUsersFlow = MutableStateFlow(emptyList<User>())
    val savedUsersFlow get() = _savedUsersFlow.asSharedFlow()

    private suspend fun setAllUsersObserver() {
        getAllUsersObserverUseCase.invoke { userDomains ->
            _allUsersFlow.value = userDomains.map { userDomain ->
                User.fromDomain(userDomain)
            }.filter { user ->
                user.uid != firebaseAuth.currentUser!!.uid
            }
        }
    }

    suspend fun getUsersForCurrentUser() {
        _savedUsersFlow.emit(
            getUsersForUserUseCase(
                uid = firebaseAuth.currentUser!!.uid
            ).map { User.fromDomain(it) }
        )
    }

}