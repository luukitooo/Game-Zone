package com.lukaarmen.domain.use_case.users

import com.lukaarmen.domain.repository.firebase.UsersRepository
import javax.inject.Inject

class UpdateUserActivityUseCase @Inject constructor(
    private val repository: UsersRepository
) {

    suspend operator fun invoke(uid: String, status: String) {
        repository.updateUserActivityStatus(uid, status)
    }

}