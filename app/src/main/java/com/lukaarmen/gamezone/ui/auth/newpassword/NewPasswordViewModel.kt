package com.lukaarmen.gamezone.ui.auth.newpassword

import android.util.Log.d
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

    private val _passwordChangeFlow = MutableSharedFlow<Boolean>()
    val passwordChangeFlow = _passwordChangeFlow.asSharedFlow()

    fun updatePassword(oldPassword: String, newPassword: String, repeatNewPassword: String){

        firebaseAuth.signInWithEmailAndPassword(firebaseAuth.currentUser!!.email.toString(), oldPassword)
            .addOnCompleteListener {signInTask ->
            if(signInTask.isSuccessful){
                if(newPassword == repeatNewPassword){
                    firebaseAuth.currentUser!!.updatePassword(newPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                viewModelScope.launch {
                                    _passwordChangeFlow.emit(true)
                                }
                            } else {
                                viewModelScope.launch {
                                    _passwordChangeFlow.emit(false)
                                }
                            }
                        }
                        .addOnFailureListener {
                            d("passwordChange", it.message.toString())
                        }
                }else{
                    viewModelScope.launch {
                        _passwordChangeFlow.emit(false)
                    }
                }
            }else{
                viewModelScope.launch {
                    _passwordChangeFlow.emit(false)
                }
            }

            }
    }
}