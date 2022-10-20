package com.lukaarmen.gamezone.ui.auth.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.lukaarmen.gamezone.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class RegistrationViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @Named("Users") private val usersReference: DatabaseReference,
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

    suspend fun saveUserToDatabase(email: String, username: String) {
        usersReference.child(firebaseAuth.currentUser?.uid!!)
            .setValue(User(firebaseAuth.currentUser?.uid!!, email, username))
    }

}