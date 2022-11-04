package com.lukaarmen.domain.usecases.users

import com.lukaarmen.domain.repositories.firebase.UsersRepository
import javax.inject.Inject

class UpdateCurrentChatUserIdUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {

    suspend operator fun invoke(uId: String, currentChatUserId: String?) =
        usersRepository.updateCurrentChatUserId(uId, currentChatUserId)
}