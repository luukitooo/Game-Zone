package com.lukaarmen.domain.use_case.leagues

import com.lukaarmen.domain.model.FavoriteLeagueDomain
import com.lukaarmen.domain.repository.FavoriteLeaguesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteLeaguesFlowByUidUseCase @Inject constructor(
    private val repository: FavoriteLeaguesRepository
) {

    operator fun invoke(uid: String): Flow<List<FavoriteLeagueDomain>> {
        return repository.getLeaguesAsFlowByUid(uid)
    }

}