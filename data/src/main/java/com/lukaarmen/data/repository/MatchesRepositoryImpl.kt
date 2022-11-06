package com.lukaarmen.data.repository

import com.lukaarmen.data.common.RequestHandler
import com.lukaarmen.data.remote.mappers.toMatchDomain
import com.lukaarmen.data.remote.service.MatchesService
import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.common.mapSuccess
import com.lukaarmen.domain.common.success
import com.lukaarmen.domain.model.MatchDomain
import com.lukaarmen.domain.repository.MatchesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MatchesRepositoryImpl @Inject constructor(
    private val matchesService: MatchesService,
    private val requestHandler: RequestHandler
) : MatchesRepository {

    override suspend fun getMatchesByLeagueId(
        gameType: String,
        timeFrame: String,
        leagueId: Int,
        sort: String,
        title: String
    ): Flow<Resource<List<MatchDomain>>> {
        return requestHandler.safeApiCall {
            matchesService.getMatchesByLeagueId(
                gameType = gameType,
                timeFrame = timeFrame,
                leagueId = leagueId,
                sort = sort,
                title = title
            )
        }.map {
            it.mapSuccess { matchDto -> matchDto.toMatchDomain() }
        }
    }

    override suspend fun getAllRunningMatches(
        sort: String,
        filter: String,
        name: String?
    ): Flow<Resource<List<MatchDomain>>> {
        return requestHandler.safeApiCall {
            matchesService.getAllRunningMatches(
                sort = sort,
                filter = filter,
                name = name
            )
        }.map {
            it.mapSuccess { matchDto ->
                matchDto.toMatchDomain()
            }
        }
    }

    override suspend fun getRunningMatchesByGame(
        gameType: String,
        sort: String,
        name: String?
    ): Flow<Resource<List<MatchDomain>>> {
        return requestHandler.safeApiCall {
            matchesService.getRunningMatchesByGame(
                gameType = gameType,
                sort = sort,
                name = name
            )
        }.map {
            it.mapSuccess { matchDto ->
                matchDto.toMatchDomain()
            }
        }
    }

    override suspend fun getMatchById(matchId: Int): Flow<Resource<MatchDomain>> {
        return requestHandler.safeApiCall {
            matchesService.getMatchById(matchId)
        }.map { matchDto ->
            matchDto.success { it.toMatchDomain() }
        }
    }

}