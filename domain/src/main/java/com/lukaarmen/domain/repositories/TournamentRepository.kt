package com.lukaarmen.domain.repositories

import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.models.TournamentsDomain
import kotlinx.coroutines.flow.Flow

interface TournamentRepository {

    suspend fun getTournamentBySerieId(
        gameType: String,
        timeFrame: String,
        page: Int,
        perPage: Int,
        serieId: Int
    ): Flow<Resource<List<TournamentsDomain>>>

}