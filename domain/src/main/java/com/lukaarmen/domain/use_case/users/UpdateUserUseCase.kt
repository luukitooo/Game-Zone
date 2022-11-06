package com.lukaarmen.domain.use_case.users

import com.lukaarmen.domain.model.UserDomain
import com.lukaarmen.domain.repository.firebase.UsersRepository
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