package com.lukaarmen.data.remote.services

import com.lukaarmen.data.remote.dto.TeamDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TeamsService {

    @GET("teams/{teamId}")
    suspend fun getTeamById(@Path("teamId") teamId: Int): Response<TeamDto>
}