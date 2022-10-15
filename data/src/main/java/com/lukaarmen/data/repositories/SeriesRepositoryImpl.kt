package com.lukaarmen.data.repositories

import com.lukaarmen.data.common.BaseRepository
import com.lukaarmen.data.remote.mappers.toSeriesDomain
import com.lukaarmen.data.remote.services.SeriesService
import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.common.mapSuccess
import com.lukaarmen.domain.models.SeriesDomain
import com.lukaarmen.domain.repositories.SeriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SeriesRepositoryImpl @Inject constructor(
    private val seriesService: SeriesService,
    private val baseRepository: BaseRepository
) : SeriesRepository {

    override suspend fun getRunningSeries(
        gameType: String,
        timeFrame: String,
        page: Int,
        perPage: Int,
        leagueId: Int
    ): Flow<Resource<List<SeriesDomain>>> {
        return baseRepository.safeApiCall {
            seriesService.getSeriesByLeagueId(gameType, timeFrame, page, perPage, leagueId)
        }.map { seriesDto ->
            seriesDto.mapSuccess { seriesDto -> seriesDto.toSeriesDomain() }
        }
    }

}