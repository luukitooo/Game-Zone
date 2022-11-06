package com.lukaarmen.domain.use_case.users

import com.lukaarmen.domain.model.UserDomain
import com.lukaarmen.domain.repository.firebase.UsersRepository
import javax.inject.Inject

class SaveUserUseCase @Inject constructor(
    private val repository: UsersRepository
) {

    suspend operator fun invoke(user: UserDomain) = repository.addUser(user)

}