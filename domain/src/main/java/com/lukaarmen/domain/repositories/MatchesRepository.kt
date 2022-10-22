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

    suspend fun getAllRunningMatches(
        page: Int?,
        perPage: Int?,
        sort: String,
        filter: String,
        name: String?
    ): Flow<Resource<List<MatchDomain>>>

    suspend fun getRunningMatchesByGame(
        gameType: String,
        page: Int,
        perPage: Int,
        sort: String,
        name: String?
    ): Flow<Resource<List<MatchDomain>>>

    suspend fun getMatchById(matchId: Int): Flow<Resource<MatchDomain>>

}