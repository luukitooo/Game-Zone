package com.lukaarmen.gamezone.ui.auth.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.lukaarmen.domain.models.firebase.UserDomain
import com.lukaarmen.domain.usecases.users.SaveUserUseCase
import com.lukaarmen.gamezone.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val saveUserUseCase: SaveUserUseCase,
) : ViewModel() {

    private val _registrationSuccessFlow = MutableSharedFlow<Boolean>()
    val registrationSuccessFlow get() = _registrationSuccessFlow.asSharedFlow()

    suspend fun createUser(email: String, password: String) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                viewModelScope.launch {
                    _registrationSuccessFlow.emit(true)
                }
            } else {
                viewModelScope.launch {
                    _registrationSuccessFlow.emit(false)
                }
            }
        }
    }

    suspend fun saveUserToFirestore(email: String, username: String) {
        saveUserUseCase.invoke(
            UserDomain(
                uid = firebaseAuth.currentUser?.uid!!,
                email = email,
                username = username
            )
        )
    }

}