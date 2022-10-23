package com.lukaarmen.data.remote.services

import com.lukaarmen.data.remote.dto.MatchDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MatchesService {

    @GET("{gameType}/matches/{timeFrame}")
    suspend fun getMatchesByLeagueId(
        @Path("gameType") gameType: String,
        @Path("timeFrame") timeFrame: String,
        @Query("page") page: Int? = null,
        @Query("per_page") perPage: Int? = null,
        @Query("filter[league_id]") leagueId: Int,
        @Query("sort") sort: String,
        @Query("search[name]") title: String = ""
    ): Response<List<MatchDto>>

    @GET("matches/running")
    suspend fun getAllRunningMatches(
        @Query("page") page: Int?,
        @Query("per_page") perPage: Int?,
        @Query("sort") sort: String,
        @Query("filter[videogame]") filter: String,
        @Query("search[name]") name: String? = null
    ): Response<List<MatchDto>>

    @GET("{gameType}/matches/running")
    suspend fun getRunningMatchesByGame(
        @Path("gameType") gameType: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("sort") sort: String,
        @Query("search[name]") name: String? = null
    ): Response<List<MatchDto>>

    @GET("matches/{matchId}")
    suspend fun getMatchById(@Path("matchId") matchId: Int): Response<MatchDto>

}