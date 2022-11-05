package com.lukaarmen.domain.usecases

import com.lukaarmen.domain.repositories.LeaguesRepository
import javax.inject.Inject

class GetLeaguesUseCase @Inject constructor(
    private val repository: LeaguesRepository
) {

    suspend operator fun invoke(
        gameType: String,
        name: String? = null
    ) = repository.getAllLeagues(
        gameType = gameType,
        name = name
    )

}