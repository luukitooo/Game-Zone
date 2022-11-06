package com.lukaarmen.domain.repository

import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.model.LeaguesDomain
import kotlinx.coroutines.flow.Flow

interface LeaguesRepository {

    suspend fun getAllLeagues(
        gameType: String,
        name: String?
    ): Flow<Resource<List<LeaguesDomain>>>
}