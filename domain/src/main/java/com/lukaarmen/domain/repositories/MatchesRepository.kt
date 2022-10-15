package com.lukaarmen.domain.repositories

import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.models.MatchDomain
import kotlinx.coroutines.flow.Flow

interface MatchesRepository {

    suspend fun getMatchesByTournamentId(
        gameType: String,
        timeFrame: String,
        page: Int,
        perPage: Int,
        tournamentId: Int,
        sort: String
    ): Flow<Resource<List<MatchDomain>>>

}