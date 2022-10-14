package com.lukaarmen.data.remote.services

import com.lukaarmen.data.remote.dto.MatchDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface MatchesService {

    @GET("{gameType}/matches/running")
    suspend fun getRunningMatches(@Path("gameType") gameType: String): Response<List<MatchDto>>

    @GET("{gameType}/matches/past")
    suspend fun getPasMatches(@Path("gameType") gameType: String): Response<List<MatchDto>>

    @GET("{gameType}/matches/upcoming")
    suspend fun getUpcomingMatches(@Path("gameType") gameType: String): Response<List<MatchDto>>
}