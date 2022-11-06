package com.lukaarmen.gamezone.ui.tabs.home.live_matches_list.match_details.share

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.domain.use_case.users.GetAllUsersUseCase
import com.lukaarmen.gamezone.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _usersFlow = MutableStateFlow(emptyList<User>())
    val usersFlow get() = _usersFlow.asStateFlow()

    init {
        viewModelScope.launch {
            _usersFlow.emit(
                getAllUsersUseCase.invoke().map { userDomain ->
                    User.fromDomain(userDomain)
                }.filter { user ->
                    user.uid != auth.currentUser!!.uid
                }
            )
        }
    }

}