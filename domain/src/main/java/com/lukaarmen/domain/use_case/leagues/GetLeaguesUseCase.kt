package com.lukaarmen.domain.use_case.leagues

import com.lukaarmen.domain.repository.LeaguesRepository
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