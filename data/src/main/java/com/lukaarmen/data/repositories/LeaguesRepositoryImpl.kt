package com.lukaarmen.data.repositories

import com.lukaarmen.data.common.RequestHandler
import com.lukaarmen.data.remote.mappers.toLeaguesDomain
import com.lukaarmen.data.remote.services.LeaguesService
import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.common.mapSuccess
import com.lukaarmen.domain.models.LeaguesDomain
import com.lukaarmen.domain.repositories.LeaguesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LeaguesRepositoryImpl @Inject constructor(
    private val leaguesService: LeaguesService,
    private val requestHandler: RequestHandler
) : LeaguesRepository {
    override suspend fun getAllLeagues(
        gameType: String,
        page: Int,
        perPage: Int
    ): Flow<Resource<List<LeaguesDomain>>> {
        return requestHandler.safeApiCall { leaguesService.getAllLeagues(gameType, page, perPage) }
            .map { it.mapSuccess { leaguesDto -> leaguesDto.toLeaguesDomain() } }
    }

}