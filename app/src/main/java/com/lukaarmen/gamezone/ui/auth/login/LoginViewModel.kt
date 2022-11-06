package com.lukaarmen.gamezone.ui.auth.login

import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.lukaarmen.domain.use_case.users.SaveUserUseCase
import com.lukaarmen.gamezone.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val saveUserUseCase: SaveUserUseCase
) : ViewModel() {

    private val _loginSuccessFlow = MutableSharedFlow<Boolean>()
    val loginSuccessFlow get() = _loginSuccessFlow.asSharedFlow()

    private val _googleSignInSuccessFlow = MutableSharedFlow<Boolean>()
    val googleSignInSuccessFlow get() = _googleSignInSuccessFlow.asSharedFlow()

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

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                viewModelScope.launch {
                    _googleSignInSuccessFlow.emit(true)
                }
            } else {
                viewModelScope.launch {
                    _googleSignInSuccessFlow.emit(false)
                }
            }
        }
    }

    fun handleGoogleSignInTask(data: Intent?) {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        if (task.isSuccessful) {
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.e("MyLog", e.message ?: "Error Without Body...")
            }
        } else {
            Log.e("MyLog", task.exception?.message ?: "Error Without Body...")
        }
    }

    suspend fun saveNewUser(user: User) {
        saveUserUseCase.invoke(
            user.toDomain()
        )
    }

}