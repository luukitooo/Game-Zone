package com.lukaarmen.domain.use_case.leagues

import com.lukaarmen.domain.repository.FavoriteLeaguesRepository
import javax.inject.Inject

class GetAllFavoriteLeaguesUseCase @Inject constructor(
    private val repository: FavoriteLeaguesRepository
) {

    suspend operator fun invoke() = repository.getAllLeagues()

}