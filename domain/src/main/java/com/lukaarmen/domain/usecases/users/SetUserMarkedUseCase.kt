package com.lukaarmen.domain.usecases.users

import com.lukaarmen.domain.repositories.firebase.UsersRepository
import javax.inject.Inject

class SetUserMarkedUseCase @Inject constructor(
    private val repository: UsersRepository
) {

    suspend operator fun invoke(selfId: String, otherId: String) {
        repository.setUserSeen(selfId, otherId)
    }

}