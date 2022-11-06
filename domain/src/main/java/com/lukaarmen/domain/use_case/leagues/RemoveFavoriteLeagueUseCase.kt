package com.lukaarmen.domain.use_case.leagues

import com.lukaarmen.domain.model.FavoriteLeagueDomain
import com.lukaarmen.domain.repository.FavoriteLeaguesRepository
import javax.inject.Inject

class RemoveFavoriteLeagueUseCase @Inject constructor(
    private val repository: FavoriteLeaguesRepository
) {

    suspend operator fun invoke(league: FavoriteLeagueDomain) {
        repository.removeLeague(league)
    }

}