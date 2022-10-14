package com.lukaarmen.data.remote.services

import com.lukaarmen.data.remote.dto.SeriesDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface SeriesService {

    @GET("{gameType}/series/running")
    suspend fun getRunningSeries(@Path("gameType") gameType: String): Response<List<SeriesDto>>

    @GET("{gameType}/series/past")
    suspend fun getPastSeries(@Path("gameType") gameType: String): Response<List<SeriesDto>>

    @GET("{gameType}/series/upcoming")
    suspend fun getUpcomingSeries(@Path("gameType") gameType: String): Response<List<SeriesDto>>
}