package com.lukaarmen.data.remote.services

import com.lukaarmen.data.remote.dto.LeaguesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LeaguesService {

    @GET("{gameType}/leagues")
    suspend fun getAllLeagues(@Path("gameType") gameType: String): Response<List<LeaguesDto>>
}