package com.lukaarmen.gamezone.ui.auth.forgot_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _resetSuccessFlow = MutableSharedFlow<Boolean>()
    val resetSuccessFlow get() = _resetSuccessFlow.asSharedFlow()

    suspend fun resetPasswordByEmail(email: String) {
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                viewModelScope.launch {
                    _resetSuccessFlow.emit(true)
                }
            } else {
                viewModelScope.launch {
                    _resetSuccessFlow.emit(false)
                }
            }
        }
    }

}