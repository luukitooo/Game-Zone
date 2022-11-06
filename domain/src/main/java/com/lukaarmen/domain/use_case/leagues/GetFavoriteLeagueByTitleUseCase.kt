package com.lukaarmen.domain.use_case.leagues

import com.lukaarmen.domain.repository.FavoriteLeaguesRepository
import javax.inject.Inject

class GetFavoriteLeagueByTitleUseCase @Inject constructor(
    private val repository: FavoriteLeaguesRepository
) {

    suspend operator fun invoke(title: String) = repository.getLeaguesByTitle(title)

}