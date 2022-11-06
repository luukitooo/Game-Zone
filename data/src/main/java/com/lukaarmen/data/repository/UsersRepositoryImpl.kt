package com.lukaarmen.data.repository

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
import com.lukaarmen.data.remote.dto.UserDto
import com.lukaarmen.domain.model.UserDomain
import com.lukaarmen.domain.repository.firebase.UsersRepository
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
        val userDto = try {
            usersCollection
                .whereEqualTo("uid", uid)
                .get()
                .await()
                .documents[0]
                .toObject(UserDto::class.java)!!
        } catch (t: Throwable) { UserDto() }
        return userDto.toUserDomain()
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

    override suspend fun updateUserDeviceId(uId: String, deviceId: String) {
        val userDto = usersCollection
            .whereEqualTo("uid", uId)
            .get()
            .await()
            .documents[0]

        usersCollection.document(userDto.id).set(
            mapOf("deviceId" to deviceId),
            SetOptions.merge()
        ).await()
    }

    override suspend fun updateCurrentChatUserId(uId: String, currentChatUserId: String?) {
        val userDto = usersCollection
            .whereEqualTo("uid", uId)
            .get()
            .await()
            .documents[0]

        usersCollection.document(userDto.id).set(
            mapOf("currentChatUseId" to currentChatUserId),
            SetOptions.merge()
        ).await()
    }

    override suspend fun observeUserById(uid: String, function: (UserDomain) -> Unit) {
        usersCollection
            .whereEqualTo("uid", uid)
            .addSnapshotListener { value, error ->
                error?.let {
                    error.printStackTrace()
                    return@addSnapshotListener
                }
                value?.documents?.get(0)?.toObject(UserDto::class.java)?.toUserDomain()?.let {
                    function.invoke(it)
                }
            }
    }

    override suspend fun setUserSeen(selfId: String, otherUserId: String) {
        val seenUserDocs = usersCollection
            .whereEqualTo("uid", selfId)
            .get()
            .await()
            .documents[0]

        val currentSeenUsers = seenUserDocs
            .toObject(UserDto::class.java)
            ?.toUserDomain()
            ?.markedUsers
            ?.toMutableList() ?: mutableListOf()

        if (!currentSeenUsers.contains(otherUserId)) {
            currentSeenUsers.add(otherUserId)
            usersCollection.document(seenUserDocs.id).set(
                mapOf("markedUsers" to currentSeenUsers),
                SetOptions.merge()
            ).await()
        }

    }

    override suspend fun removeUserSeen(selfId: String, otherUserId: String) {
        val seenUserDocs = usersCollection
            .whereEqualTo("uid", selfId)
            .get()
            .await()
            .documents[0]

        val currentSeenUsers = seenUserDocs
            .toObject(UserDto::class.java)
            ?.toUserDomain()
            ?.markedUsers
            ?.toMutableList() ?: mutableListOf()

        if (currentSeenUsers.contains(otherUserId)) {
            currentSeenUsers.remove(otherUserId)
            usersCollection.document(seenUserDocs.id).set(
                mapOf("markedUsers" to currentSeenUsers),
                SetOptions.merge()
            ).await()
        }
    }
}
