package com.lukaarmen.domain.usecases.users

import com.lukaarmen.domain.repositories.firebase.UsersRepository
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val repository: UsersRepository
) {

    suspend operator fun invoke() = repository.getAllUsers()

}