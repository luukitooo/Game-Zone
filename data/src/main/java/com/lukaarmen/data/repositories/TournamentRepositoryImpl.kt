package com.lukaarmen.data.repositories

import com.lukaarmen.data.common.RequestHandler
import com.lukaarmen.data.remote.mappers.toTournamentsDomain
import com.lukaarmen.data.remote.services.TournamentsService
import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.common.mapSuccess
import com.lukaarmen.domain.models.TournamentsDomain
import com.lukaarmen.domain.repositories.TournamentRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TournamentRepositoryImpl @Inject constructor(
    private val tournamentsService: TournamentsService,
    private val requestHandler: RequestHandler
) : TournamentRepository {

    override suspend fun getTournamentBySerieId(
        gameType: String,
        timeFrame: String,
        page: Int,
        perPage: Int,
        serieId: Int
    ): Flow<Resource<List<TournamentsDomain>>> {
        return requestHandler.safeApiCall {
            tournamentsService.getTournamentsBySerieId(
                gameType = gameType,
                timeFrame = timeFrame,
                page = page,
                perPage = perPage,
                leagueId = serieId
            )
        }.map {
            it.mapSuccess { tournamentDto -> tournamentDto.toTournamentsDomain() }
        }
    }
}