package com.lukaarmen.domain.repository.firebase

import com.lukaarmen.domain.model.UserDomain

interface UsersRepository {

    suspend fun addUser(user: UserDomain)

    suspend fun getAllUsers(): List<UserDomain>

    suspend fun getUserById(uid: String): UserDomain

    suspend fun changeUser(oldUser: UserDomain, newUser: Map<String, Any>)

    suspend fun observeAllUsers(function: (List<UserDomain>) -> Unit)

    suspend fun updateUserActivityStatus(uid: String, status: String)

    suspend fun saveOtherUserId(selfId: String, otherUserId: String)

    suspend fun getUsersForUser(uid: String) : List<UserDomain>

    suspend fun updateUserDeviceId(uId: String, deviceId: String)

    suspend fun updateCurrentChatUserId(uId: String, currentChatUserId: String?)

    suspend fun observeUserById(uid: String, function: (UserDomain) -> Unit)

    suspend fun setUserSeen(selfId: String, otherUserId: String)

    suspend fun removeUserSeen(selfId: String, otherUserId: String)

}