package com.lukaarmen.domain.use_case.users

import com.lukaarmen.domain.repository.firebase.UsersRepository
import javax.inject.Inject

class UpdateUserDeviceIdUseCase @Inject constructor(
    private val usersRepository: UsersRepository
) {

    suspend operator fun invoke(uId: String, deviceId: String) = usersRepository.updateUserDeviceId(uId, deviceId)

}