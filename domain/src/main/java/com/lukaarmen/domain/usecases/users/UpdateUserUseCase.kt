package com.lukaarmen.domain.usecases.users

import com.lukaarmen.domain.models.firebase.UserDomain
import com.lukaarmen.domain.repositories.firebase.UsersRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val repository: UsersRepository
) {

    suspend operator fun invoke(oldUser: UserDomain, newUser: Map<String, Any>) {
        repository.changeUser(
            oldUser = oldUser,
            newUser = newUser
        )
    }

}