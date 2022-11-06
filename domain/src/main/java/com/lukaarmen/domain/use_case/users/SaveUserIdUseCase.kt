package com.lukaarmen.domain.use_case.users

import com.lukaarmen.domain.repository.firebase.UsersRepository
import javax.inject.Inject

class SaveUserIdUseCase @Inject constructor(
    private val repository: UsersRepository
) {

    suspend operator fun invoke(selfId: String, otherUserId: String) {
        repository.saveOtherUserId(
            selfId = selfId,
            otherUserId = otherUserId
        )
    }

}