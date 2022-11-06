package com.lukaarmen.domain.use_case.users

import com.lukaarmen.domain.repository.firebase.UsersRepository
import javax.inject.Inject

class UpdateCurrentChatUserIdUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {

    suspend operator fun invoke(uId: String, currentChatUserId: String?) =
        usersRepository.updateCurrentChatUserId(uId, currentChatUserId)
}