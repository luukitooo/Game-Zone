package com.lukaarmen.domain.usecases

import com.lukaarmen.domain.repositories.LeaguesRepository
import javax.inject.Inject

class GetLeaguesUseCase @Inject constructor(
    private val repository: LeaguesRepository
) {

    suspend operator fun invoke(
        gameType: String,
        page: Int,
        perPage: Int,
        name: String? = null
    ) = repository.getAllLeagues(
        gameType = gameType,
        page = page,
        perPage = perPage,
        name = name
    )

}