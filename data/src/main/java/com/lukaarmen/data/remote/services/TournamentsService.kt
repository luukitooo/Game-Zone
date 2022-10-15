package com.lukaarmen.data.remote.services

import com.lukaarmen.data.remote.dto.TournamentDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TournamentsService {

    @GET("{gameType}/tournaments/{timeFrame}")
    suspend fun getTournamentsBySerieId(
        @Path("gameType") gameType: String,
        @Path("timeFrame") timeFrame: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("filter[serie_id]") leagueId: Int,
    ): Response<List<TournamentDto>>
}