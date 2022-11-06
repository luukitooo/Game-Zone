package com.lukaarmen.data.repository

import com.lukaarmen.data.common.RequestHandler
import com.lukaarmen.data.remote.mappers.toTeamDomain
import com.lukaarmen.data.remote.service.TeamsService
import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.common.success
import com.lukaarmen.domain.model.TeamDomain
import com.lukaarmen.domain.repository.TeamsRepository
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