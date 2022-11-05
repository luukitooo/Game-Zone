package com.lukaarmen.domain.usecases.users

import com.lukaarmen.domain.models.firebase.UserDomain
import com.lukaarmen.domain.repositories.firebase.UsersRepository
import javax.inject.Inject

class ObserveUserByIdUseCase @Inject constructor(
    private val repository: UsersRepository
) {

    suspend operator fun invoke(uid: String, function: (UserDomain) -> Unit) {
        repository.observeUserById(uid, function)
    }

}