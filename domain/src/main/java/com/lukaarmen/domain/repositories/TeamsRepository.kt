package com.lukaarmen.domain.repositories

import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.models.TeamDomain
import kotlinx.coroutines.flow.Flow

interface TeamsRepository {

    suspend fun getTeamById(teamId: Int): Flow<Resource<TeamDomain>>

}