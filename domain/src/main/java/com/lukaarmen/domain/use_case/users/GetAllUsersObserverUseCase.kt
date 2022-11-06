package com.lukaarmen.domain.use_case.users

import com.lukaarmen.domain.model.UserDomain
import com.lukaarmen.domain.repository.firebase.UsersRepository
import javax.inject.Inject

class GetAllUsersObserverUseCase @Inject constructor(
    private val repository: UsersRepository
) {

    suspend operator fun invoke(function: (List<UserDomain>) -> Unit) {
        repository.observeAllUsers { users ->
            function.invoke(users)
        }
    }

}