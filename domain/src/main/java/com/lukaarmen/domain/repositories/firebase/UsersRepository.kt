package com.lukaarmen.domain.repositories.firebase

import com.lukaarmen.domain.models.firebase.UserDomain

interface UsersRepository {

    suspend fun addUser(user: UserDomain)

    suspend fun getAllUsers(): List<UserDomain>

    suspend fun getUserById(uid: String): UserDomain

    suspend fun changeUser(oldUser: UserDomain, newUser: Map<String, Any>)

    suspend fun observeAllUsers(function: (List<UserDomain>) -> Unit)

}