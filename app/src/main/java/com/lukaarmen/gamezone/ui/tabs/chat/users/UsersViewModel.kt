package com.lukaarmen.gamezone.ui.tabs.chat.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.domain.use_case.users.GetAllUsersObserverUseCase
import com.lukaarmen.domain.use_case.users.GetUserByIdUseCase
import com.lukaarmen.domain.use_case.users.GetUsersForUserUseCase
import com.lukaarmen.gamezone.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val getAllUsersObserverUseCase: GetAllUsersObserverUseCase,
    private val getUsersForUserUseCase: GetUsersForUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            getUsersForCurrentUser()
        }
        viewModelScope.launch {
            setAllUsersObserver()
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
                user.uid != (firebaseAuth.currentUser?.uid ?: "")
            }
            viewModelScope.launch {
                getUsersForCurrentUser()
            }
        }
    }

    suspend fun getUsersForCurrentUser() {
        val markedUsers = getUserByIdUseCase.invoke(firebaseAuth.currentUser!!.uid).markedUsers ?: emptyList()
        val usersForCurrentUser = getUsersForUserUseCase(uid = firebaseAuth.currentUser!!.uid).map { userDomain ->
            User.fromDomain(userDomain)
        }.toMutableList()
        usersForCurrentUser.forEach { user ->
            if (markedUsers.contains(user.uid))
                user.isMarked = true
        }
        _savedUsersFlow.emit(usersForCurrentUser.sortedByDescending { it.isMarked })
    }

}