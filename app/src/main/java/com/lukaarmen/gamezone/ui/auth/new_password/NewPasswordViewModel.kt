package com.lukaarmen.gamezone.ui.auth.new_password

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NewPasswordViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
): ViewModel() {

    private val _passwordChangeSuccessFlow = MutableSharedFlow<Boolean>()
    val passwordChangeSuccessFlow = _passwordChangeSuccessFlow.asSharedFlow()

    fun updatePasswordByEmail(email: String){
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful)
                viewModelScope.launch { _passwordChangeSuccessFlow.emit(true) }
            else
                viewModelScope.launch { _passwordChangeSuccessFlow.emit(false) }
        }
    }
}