package com.lukaarmen.domain.usecases.users

import com.lukaarmen.domain.models.firebase.UserDomain
import com.lukaarmen.domain.repositories.firebase.UsersRepository
import javax.inject.Inject

class GetUsersForUserUseCase @Inject constructor(
    private val repository: UsersRepository
) {

    suspend operator fun invoke(uid: String) : List<UserDomain> {
        return repository.getUsersForUser(uid)
    }

}