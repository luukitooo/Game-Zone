package com.lukaarmen.domain.usecases.users

import com.lukaarmen.domain.repositories.firebase.UsersRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(
    private val repository: UsersRepository
) {

    suspend operator fun invoke(uid: String) = repository.getUserById(uid)

}