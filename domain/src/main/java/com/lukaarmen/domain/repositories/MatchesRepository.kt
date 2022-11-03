package com.lukaarmen.domain.repositories

import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.models.MatchDomain
import kotlinx.coroutines.flow.Flow

interface MatchesRepository {

    suspend fun getMatchesByLeagueId(
        gameType: String,
        timeFrame: String,
        leagueId: Int,
        sort: String,
        title: String
    ): Flow<Resource<List<MatchDomain>>>

    suspend fun getAllRunningMatches(
        sort: String,
        filter: String,
        name: String?
    ): Flow<Resource<List<MatchDomain>>>

    suspend fun getRunningMatchesByGame(
        gameType: String,
        sort: String,
        name: String?
    ): Flow<Resource<List<MatchDomain>>>

    suspend fun getMatchById(matchId: Int): Flow<Resource<MatchDomain>>

}