package com.lukaarmen.gamezone.ui.profile.profilefragment

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log.d
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.StorageReference
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
    @Named("Users") private val usersReference: DatabaseReference,
    @Named("ProfilePictures") private val storageReference: StorageReference
) : ViewModel() {

    init {
        viewModelScope.launch { updateProfile() }
    }

    private val _userState = MutableStateFlow(User())
    val userSate = _userState.asStateFlow()

    private val _photoUploadProgressFlow = MutableStateFlow(0L)
    val photoUploadProgressFlow = _photoUploadProgressFlow.asStateFlow()

    fun signOut() {
        firebaseAuth.signOut()
    }

    private fun updateProfile() {
        usersReference.child(firebaseAuth.currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val user = snapshot.getValue(User::class.java) ?: return
                    _userState.value = user
                }

                override fun onCancelled(error: DatabaseError) {
                    throw Exception(error.message)
                }
            })
    }

    fun uploadImageToStorage(drawable: BitmapDrawable) {
        val bitmap = drawable.bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val data = baos.toByteArray()
        val userImageReference = storageReference.child(firebaseAuth.currentUser!!.uid)
        userImageReference.putBytes(data)
            .addOnProgressListener{progressSnapshot ->
                val progress = (100*progressSnapshot.bytesTransferred)/progressSnapshot.totalByteCount
                _photoUploadProgressFlow.value = progress
            }
            .addOnFailureListener { exception ->
                throw exception
            }.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception ?: Exception()
                }
                userImageReference.downloadUrl
            }.addOnCompleteListener { task ->
                usersReference.child(firebaseAuth.currentUser!!.uid)
                    .child("imageUrl")
                    .setValue(task.result.toString())
            }
    }

}