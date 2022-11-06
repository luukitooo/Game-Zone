package com.lukaarmen.gamezone.ui.auth.login.enter_username

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.domain.use_case.users.GetUserByIdUseCase
import com.lukaarmen.domain.use_case.users.UpdateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EnterUsernameViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val updateUserUseCase: UpdateUserUseCase,
    private val getUserByIdUseCase: GetUserByIdUseCase
) : ViewModel() {

    suspend fun updateUsername(newUsername: String) {
        updateUserUseCase.invoke(
            oldUser = getUserByIdUseCase.invoke(auth.currentUser!!.uid),
            newUser = mapOf(
                "username" to newUsername
            )
        )
    }

}