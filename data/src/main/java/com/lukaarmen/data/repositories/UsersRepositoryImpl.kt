package com.lukaarmen.data.repositories

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.lukaarmen.data.remote.dto.UserDto
import com.lukaarmen.domain.models.firebase.UserDomain
import com.lukaarmen.domain.repositories.firebase.UsersRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named

class UsersRepositoryImpl @Inject constructor(
    @Named("Users") private val usersCollection: CollectionReference
) : UsersRepository {

    override suspend fun addUser(user: UserDomain) {
        usersCollection.add(
            UserDto.fromDomain(user)
        ).await()
        usersCollection.document()
    }

    override suspend fun getAllUsers(): List<UserDomain> {
        return usersCollection.get().await().documents.map { document ->
            document.toObject(UserDto::class.java)?.toUserDomain() ?: return emptyList()
        }
    }

    override suspend fun getUserById(uid: String): UserDomain {
        val userDto = usersCollection
            .whereEqualTo("uid", uid)
            .get()
            .await()
            .documents[0]
            .toObject(UserDto::class.java)
        return userDto?.toUserDomain() ?: UserDomain()
    }

    override suspend fun changeUser(oldUser: UserDomain, newUser: Map<String, Any>) {
        val userDto = usersCollection
            .whereEqualTo("uid", oldUser.uid)
            .get()
            .await()
            .documents[0]

        usersCollection.document(userDto.id).set(
            newUser,
            SetOptions.merge()
        ).await()
    }

    override suspend fun observeAllUsers(function: (List<UserDomain>) -> Unit) {
        usersCollection.addSnapshotListener { value, error ->
            error?.let { exception ->
                exception.printStackTrace()
                return@addSnapshotListener
            }

            val usersList = value?.documents?.map {
                it.toObject(UserDomain::class.java) ?: UserDomain()
            } ?: emptyList()
            function.invoke(usersList)
        }
    }

    override suspend fun updateUserActivityStatus(uid: String, status: String) {
        val userDto = usersCollection
            .whereEqualTo("uid", uid)
            .get()
            .await()
            .documents[0]

        usersCollection.document(userDto.id).set(
            mapOf("activity" to status),
            SetOptions.merge()
        ).await()
    }

    override suspend fun saveOtherUserId(selfId: String, otherUserId: String) {
        val currentUserDoc = usersCollection
            .whereEqualTo("uid", selfId)
            .get()
            .await()
            .documents[0]

        val savedUsers = currentUserDoc
            .toObject(UserDto::class.java)
            ?.savedUserIds
            ?.toMutableList() ?: mutableListOf()

        if (!savedUsers.contains(otherUserId)) {
            savedUsers.add(otherUserId)
            usersCollection.document(currentUserDoc.id).set(
                mapOf("savedUserIds" to savedUsers),
                SetOptions.merge()
            ).await()
        }
    }

    override suspend fun getUsersForUser(uid: String): List<UserDomain> {
        val user = usersCollection
            .whereEqualTo("uid", uid)
            .get()
            .await()
            .documents[0]
            .toObject(UserDto::class.java)?.toUserDomain() ?: UserDomain()
        return List(user.savedUserIds?.size ?: 0) {
            CoroutineScope(Dispatchers.Main).async {
                getUserById(user.savedUserIds!![it])
            }.await()
        }
    }
}