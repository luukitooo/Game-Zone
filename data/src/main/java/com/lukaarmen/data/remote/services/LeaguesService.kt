package com.lukaarmen.data.remote.services

import com.lukaarmen.data.remote.dto.LeaguesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LeaguesService {

    @GET("{gameType}/leagues")
    suspend fun getAllLeagues(
        @Path("gameType") gameType: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("search[name]") name: String? = null
    ): Response<List<LeaguesDto>>
}