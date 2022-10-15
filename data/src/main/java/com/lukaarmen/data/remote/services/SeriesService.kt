package com.lukaarmen.data.remote.services

import com.lukaarmen.data.remote.dto.SeriesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SeriesService {

    @GET("{gameType}/series/{timeFrame}")
    suspend fun getSeriesByLeagueId(
        @Path("gameType") gameType: String,
        @Path("timeFrame") timeFrame: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("filter[league_id]") leagueId: Int,
    ): Response<List<SeriesDto>>
}