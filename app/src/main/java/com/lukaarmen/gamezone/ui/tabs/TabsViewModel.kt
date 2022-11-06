package com.lukaarmen.gamezone.ui.tabs

import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.lukaarmen.domain.use_case.users.UpdateUserDeviceIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TabsViewModel @Inject constructor(
    private val firebaseMessaging: FirebaseMessaging,
    private val setUserDeviceIdUseCase: UpdateUserDeviceIdUseCase,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    suspend fun setToken(){
        firebaseMessaging.token.addOnCompleteListener{tokenTask ->
            if(tokenTask.isSuccessful){
                d("token_error", "token success")
                viewModelScope.launch {
                    setUserDeviceIdUseCase(firebaseAuth.currentUser!!.uid, tokenTask.result)
                }
            }else{
                d("token_error", "token error")
            }
        }
    }

}