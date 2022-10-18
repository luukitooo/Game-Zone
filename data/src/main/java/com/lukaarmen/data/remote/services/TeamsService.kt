package com.lukaarmen.data.remote.services

import com.lukaarmen.data.remote.dto.TeamDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TeamsService {

    @GET("{gameType}/teams")
    suspend fun getAllTeams(@Path("gameType") gameType: String): Response<List<TeamDto>>
}