package com.lukaarmen.gamezone.ui.profile.profilefragment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
import com.lukaarmen.domain.models.firebase.UserDomain
import com.lukaarmen.domain.usecases.users.GetUserByIdUseCase
import com.lukaarmen.domain.usecases.users.UpdateUserUseCase
import com.lukaarmen.gamezone.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    @Named("ProfilePictures") private val storageReference: StorageReference
) : ViewModel() {

    init {
        viewModelScope.launch {
            updateProfile()
        }
    }

    private val _userState = MutableStateFlow(User())
    val userSate = _userState.asStateFlow()

    fun signOut() {
        firebaseAuth.signOut()
    }

    suspend fun updateProfile() {
        _userState.emit(
            User.fromDomain(getUserByIdUseCase.invoke(firebaseAuth.currentUser!!.uid))
        )
    }

    suspend fun uploadImageToStorage(drawable: BitmapDrawable) {
        val bitmap = drawable.bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val data = baos.toByteArray()
        val userImageReference = storageReference.child(firebaseAuth.currentUser!!.uid)
        userImageReference.putBytes(data).addOnFailureListener { exception ->
            throw exception
        }.continueWithTask { task ->
            if (!task.isSuccessful){
                throw task.exception ?: Exception()
            }
            userImageReference.downloadUrl
        }.addOnCompleteListener { task ->
            viewModelScope.launch {
                updateUserUseCase.invoke(
                    oldUser = _userState.value.toDomain(),
                    newUser = mapOf("imageUrl" to task.result.toString())
                )
            }.invokeOnCompletion {
                viewModelScope.launch { updateProfile() }
            }
        }
    }

}