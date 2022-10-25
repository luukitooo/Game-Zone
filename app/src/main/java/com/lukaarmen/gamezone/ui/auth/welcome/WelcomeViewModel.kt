package com.lukaarmen.gamezone.ui.auth.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _isLoggedIn = MutableSharedFlow<Boolean>()
    val isLoggedIn = _isLoggedIn.asSharedFlow()

    init {
        viewModelScope.launch {
            if(firebaseAuth.currentUser != null) _isLoggedIn.emit(true)
            else _isLoggedIn.emit(false)
        }
    }

}