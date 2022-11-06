package com.lukaarmen.domain.use_case.users

import com.lukaarmen.domain.model.UserDomain
import com.lukaarmen.domain.repository.firebase.UsersRepository
import javax.inject.Inject

class GetUsersForUserUseCase @Inject constructor(
    private val repository: UsersRepository
) {

    suspend operator fun invoke(uid: String) : List<UserDomain> {
        return repository.getUsersForUser(uid)
    }

}