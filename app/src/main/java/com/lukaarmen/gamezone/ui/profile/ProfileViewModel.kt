package com.lukaarmen.gamezone.ui.profile

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.StorageReference
import com.lukaarmen.domain.use_case.users.GetUserByIdUseCase
import com.lukaarmen.domain.use_case.users.UpdateUserDeviceIdUseCase
import com.lukaarmen.domain.use_case.users.UpdateUserUseCase
import com.lukaarmen.gamezone.R
import com.lukaarmen.gamezone.model.User
import com.lukaarmen.gamezone.ui.profile.settings.SettingsGroup
import com.lukaarmen.gamezone.ui.profile.settings.SettingsType
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
    private val updateUserDeviceIdUseCase: UpdateUserDeviceIdUseCase,
    @Named("ProfilePictures") private val storageReference: StorageReference
) : ViewModel() {

    private val _userState = MutableStateFlow(User())
    val userSate get() = _userState.asStateFlow()

    fun signOut() {
        viewModelScope.launch {
            updateUserDeviceIdUseCase(firebaseAuth.currentUser!!.uid, "")
        }
        firebaseAuth.signOut()
    }

    suspend fun updateProfile() {
        _userState.emit(
            User.fromDomain(
                getUserByIdUseCase.invoke(
                    firebaseAuth.currentUser!!.uid
                )
            )
        )
    }

    fun uploadImageToStorage(drawable: BitmapDrawable) {
        val bitmap = drawable.bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos)
        val data = baos.toByteArray()
        val userImageReference = storageReference.child(firebaseAuth.currentUser!!.uid)
        userImageReference.putBytes(data)
            .addOnFailureListener { exception ->
                throw exception
            }.continueWithTask { task ->
                if (!task.isSuccessful) {
                    throw task.exception ?: Exception()
                }
                userImageReference.downloadUrl
            }.addOnCompleteListener { task ->
                viewModelScope.launch {
                    updateUserUseCase.invoke(
                        oldUser = _userState.value.toDomain(),
                        newUser = mapOf(
                            "imageUrl" to task.result.toString()
                        )
                    )
                    updateProfile()
                }
            }
    }

    val settings = listOf(
        SettingsGroup(
            title = "Edit profile",
            list = listOf(
                SettingsGroup.SettingItem(SettingsType.USERNAME, R.drawable.ic_user, SettingsType.USERNAME.title),
                SettingsGroup.SettingItem(SettingsType.PASSWORD, R.drawable.ic_lock, SettingsType.PASSWORD.title),
                SettingsGroup.SettingItem(SettingsType.PHOTO, R.drawable.ic_photo, SettingsType.PHOTO.title)
            )
        ),
        SettingsGroup(
            title = "Info",
            list = listOf(
                SettingsGroup.SettingItem(SettingsType.HELP, R.drawable.ic_help, SettingsType.HELP.title),
                SettingsGroup.SettingItem(SettingsType.ABOUT_US, R.drawable.ic_info, SettingsType.ABOUT_US.title)
            )
        ),
        SettingsGroup(
            title = "Other",
            list = listOf(
                SettingsGroup.SettingItem(SettingsType.SIGN_OUT, R.drawable.ic_signout, SettingsType.SIGN_OUT.title)
            )
        )
    )

}