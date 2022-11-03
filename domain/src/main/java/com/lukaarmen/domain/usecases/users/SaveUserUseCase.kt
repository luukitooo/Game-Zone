package com.lukaarmen.domain.usecases.users

import com.lukaarmen.domain.models.firebase.UserDomain
import com.lukaarmen.domain.repositories.firebase.UsersRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val repository: UsersRepository
) {

    suspend operator fun invoke(user: UserDomain) = repository.addUser(user)

}