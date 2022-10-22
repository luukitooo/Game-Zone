package com.lukaarmen.domain.usecases.favorite_leagues

import com.lukaarmen.domain.models.FavoriteLeagueDomain
import com.lukaarmen.domain.repositories.FavoriteLeaguesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteLeaguesFlowByUidUseCase @Inject constructor(
    private val repository: FavoriteLeaguesRepository
) {

    operator fun invoke(uid: String): Flow<List<FavoriteLeagueDomain>> {
        return repository.getLeaguesAsFlowByUid(uid)
    }

}