package com.lukaarmen.domain.repositories

import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.models.LeaguesDomain
import kotlinx.coroutines.flow.Flow

interface LeaguesRepository {

    suspend fun getAllLeagues(
        gameType: String,
        page: Int,
        perPage: Int
    ): Flow<Resource<List<LeaguesDomain>>>
}