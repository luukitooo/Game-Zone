package com.lukaarmen.data.repositories

import com.lukaarmen.data.common.BaseRepository
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
    private val baseRepository: BaseRepository
) : MatchesRepository {

    override suspend fun getMatchesByLeagueId(
        gameType: String,
        timeFrame: String,
        page: Int,
        perPage: Int,
        leagueId: Int,
        sort: String,
        title: String
    ): Flow<Resource<List<MatchDomain>>> {
        return baseRepository.safeApiCall {
            matchesService.getMatchesByLeagueId(
                gameType = gameType,
                timeFrame = timeFrame,
                page = page,
                perPage = perPage,
                leagueId = leagueId,
                sort = sort,
                title = title
            )
        }.map {
            it.mapSuccess { matchDto -> matchDto.toMatchDomain() }
        }
    }

    override suspend fun getAllRunningMatches(
        page: Int?,
        perPage: Int?,
        sort: String,
        filter: String
    ): Flow<Resource<List<MatchDomain>>> {
        return baseRepository.safeApiCall {
            matchesService.getAllRunningMatches(
                page = page,
                perPage = perPage,
                sort = sort,
                filter = filter
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
        sort: String
    ): Flow<Resource<List<MatchDomain>>> {
        return baseRepository.safeApiCall {
            matchesService.getRunningMatchesByGame(
                gameType = gameType,
                page = page,
                perPage = perPage,
                sort = sort
            )
        }.map {
            it.mapSuccess { matchDto ->
                matchDto.toMatchDomain()
            }
        }
    }

}