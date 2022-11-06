package com.lukaarmen.domain.repository

import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.model.TeamDomain
import kotlinx.coroutines.flow.Flow

interface TeamsRepository {

    suspend fun getTeamById(teamId: Int): Flow<Resource<TeamDomain>>

}