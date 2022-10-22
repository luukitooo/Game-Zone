package com.lukaarmen.domain.usecases.favorite_leagues

import com.lukaarmen.domain.repositories.FavoriteLeaguesRepository
import javax.inject.Inject

class GetFavoriteLeagueByTitleUseCase @Inject constructor(
    private val repository: FavoriteLeaguesRepository
) {

    suspend operator fun invoke(title: String) = repository.getLeaguesByTitle(title)

}