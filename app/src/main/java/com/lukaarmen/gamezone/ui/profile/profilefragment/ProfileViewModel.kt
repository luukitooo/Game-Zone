package com.lukaarmen.gamezone.ui.profile.profilefragment

import android.net.Uri
import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.lukaarmen.gamezone.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.*
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @Named("Users") private val usersReference: DatabaseReference,
) : ViewModel() {

    private val _userState = MutableStateFlow(firebaseAuth.currentUser)
    val userSate = _userState.asStateFlow()

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun setProfilePicture(data: Uri?) {
        firebaseAuth.currentUser?.let { user ->
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setPhotoUri(data)
                .build()

            viewModelScope.launch {
                try {
                    user.updateProfile(profileUpdates)
                    d("pic_update", "success")
                } catch (e: Throwable) {
                    d("pic_update", "error")
                }
            }
        }
    }
}