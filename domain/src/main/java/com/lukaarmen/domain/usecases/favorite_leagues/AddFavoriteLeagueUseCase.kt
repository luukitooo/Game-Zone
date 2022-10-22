package com.lukaarmen.domain.usecases.favorite_leagues

import com.lukaarmen.domain.models.FavoriteLeagueDomain
import com.lukaarmen.domain.repositories.FavoriteLeaguesRepository
import javax.inject.Inject

class AddFavoriteLeagueUseCase @Inject constructor(
    private val repository: FavoriteLeaguesRepository
) {

    suspend operator fun invoke(league: FavoriteLeagueDomain) {
        repository.addLeague(league)
    }

}