package com.lukaarmen.domain.usecases.users

import com.lukaarmen.domain.models.firebase.UserDomain
import com.lukaarmen.domain.repositories.firebase.UsersRepository
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