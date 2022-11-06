package com.lukaarmen.data.remote.service

import com.lukaarmen.data.remote.dto.LeaguesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LeaguesService {

    @GET("{gameType}/leagues")
    suspend fun getAllLeagues(
        @Path("gameType") gameType: String,
        @Query("search[name]") name: String? = null
    ): Response<List<LeaguesDto>>
}