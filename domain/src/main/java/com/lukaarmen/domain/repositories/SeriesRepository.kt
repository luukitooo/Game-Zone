package com.lukaarmen.domain.repositories

import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.models.SeriesDomain
import kotlinx.coroutines.flow.Flow

interface SeriesRepository {

    suspend fun getRunningSeries(gameType: String, timeFrame: String, page: Int, perPage: Int, leagueId: Int): Flow<Resource<List<SeriesDomain>>>

}