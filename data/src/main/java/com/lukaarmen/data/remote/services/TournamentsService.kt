package com.lukaarmen.data.remote.services

import com.lukaarmen.data.remote.dto.TournamentDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TournamentsService {

    @GET("{gameType}/tournaments/running")
    suspend fun getRunningTournaments(@Path("gameType") gameType: String): Response<List<TournamentDto>>

    @GET("{gameType}/tournaments/past")
    suspend fun getPastTournaments(@Path("gameType") gameType: String): Response<List<TournamentDto>>

    @GET("{gameType}/tournaments/upcoming")
    suspend fun getUpcomingTournaments(@Path("gameType") gameType: String): Response<List<TournamentDto>>
}