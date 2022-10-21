package com.lukaarmen.data.repositories

import com.lukaarmen.data.common.RequestHandler
import com.lukaarmen.data.remote.mappers.toMatchDomain
import com.lukaarmen.data.remote.services.MatchesService
import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.common.mapSuccess
import com.lukaarmen.domain.models.MatchDomain
import com.lukaarmen.domain.repositories.MatchesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MatchesRepositoryImpl @Inject constructor(
    private val matchesService: MatchesService,
    private val requestHandler: RequestHandler
) : MatchesRepository {
    override suspend fun getMatchesByTournamentId(
        gameType: String,
        timeFrame: String,
        page: Int,
        perPage: Int,
        tournamentId: Int,
        sort: String
    ): Flow<Resource<List<MatchDomain>>> {
        return requestHandler.safeApiCall {
            matchesService.getMatchesByTournamentId(
                gameType = gameType,
                timeFrame = timeFrame,
                page = page,
                perPage = perPage,
                leagueId = tournamentId,
                sort = sort
            )
        }.map {
            it.mapSuccess { matchDto -> matchDto.toMatchDomain() }
        }
    }

    override suspend fun getAllRunningMatches(
        page: Int?,
        perPage: Int?,
        sort: String,
        filter: String,
        name: String?
    ): Flow<Resource<List<MatchDomain>>> {
        return requestHandler.safeApiCall {
            matchesService.getAllRunningMatches(
                page = page,
                perPage = perPage,
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
        page: Int,
        perPage: Int,
        sort: String,
        name: String?
    ): Flow<Resource<List<MatchDomain>>> {
        return requestHandler.safeApiCall {
            matchesService.getRunningMatchesByGame(
                gameType = gameType,
                page = page,
                perPage = perPage,
                sort = sort,
                name = name
            )
        }.map {
            it.mapSuccess { matchDto ->
                matchDto.toMatchDomain()
            }
        }
    }

}