package com.lukaarmen.domain.use_case.users

import com.lukaarmen.domain.repository.firebase.UsersRepository
import javax.inject.Inject

class RemoveUserMarkedUseCase @Inject constructor(
    private val repository: UsersRepository
) {

    suspend operator fun invoke(selfId: String, otherId: String) {
        repository.removeUserSeen(selfId, otherId)
    }

}