package com.lukaarmen.data.repositories

import com.lukaarmen.data.common.RequestHandler
import com.lukaarmen.data.remote.mappers.toTeamDomain
import com.lukaarmen.data.remote.services.TeamsService
import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.common.success
import com.lukaarmen.domain.models.TeamDomain
import com.lukaarmen.domain.repositories.TeamsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TeamsRepositoryImpl @Inject constructor(
    private val teamsService: TeamsService,
    private val requestHandler: RequestHandler
): TeamsRepository {

    override suspend fun getTeamById(teamId: Int): Flow<Resource<TeamDomain>> {
        return requestHandler.safeApiCall {
            teamsService.getTeamById(teamId)
        }.map {
            it.success { teamDto -> teamDto.toTeamDomain() }
        }
    }
}