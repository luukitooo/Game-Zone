package com.lukaarmen.gamezone.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _loginSuccessFlow = MutableSharedFlow<Boolean>()
    val loginSuccessFlow get() = _loginSuccessFlow.asSharedFlow()

    suspend fun loginWith(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                viewModelScope.launch {
                    _loginSuccessFlow.emit(true)
                }
            } else {
                viewModelScope.launch {
                    _loginSuccessFlow.emit(false)
                }
            }
        }
    }

}