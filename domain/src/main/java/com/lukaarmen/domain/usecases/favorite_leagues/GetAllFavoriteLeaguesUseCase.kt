package com.lukaarmen.domain.usecases.favorite_leagues

import com.lukaarmen.domain.repositories.FavoriteLeaguesRepository
import javax.inject.Inject

class GetAllFavoriteLeaguesUseCase @Inject constructor(
    private val repository: FavoriteLeaguesRepository
) {

    suspend operator fun invoke() = repository.getAllLeagues()

}