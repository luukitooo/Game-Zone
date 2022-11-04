package com.lukaarmen.gamezone.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.lukaarmen.gamezone.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseMessaging(): FirebaseMessaging{
        return FirebaseMessaging.getInstance()
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthInstance(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Provides
    @Singleton
    @Named("Users")
    fun provideDatabaseUsersReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("Users")
    }

    @Provides
    @Singleton
    fun provideGoogleSignInClient(
        @ApplicationContext context: Context
    ): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder()
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    @Provides
    @Singleton
    @Named("ProfilePictures")
    fun provideFirebaseStorageReference(): StorageReference {
        return FirebaseStorage.getInstance().getReference("UserProfilePictures")
    }

    @Provides
    @Singleton
    @Named("Messages")
    fun provideDatabaseMessagesReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReference("Messages")
    }

    @Provides
    @Singleton
    @Named("Users")
    fun provideFirestoreUsersReference(): CollectionReference {
        return Firebase.firestore.collection("Users")
    }

    @Provides
    @Singleton
    @Named("Chats")
    fun provideFirestoreChatsReference(): CollectionReference {
        return Firebase.firestore.collection("Chats")
    }

}