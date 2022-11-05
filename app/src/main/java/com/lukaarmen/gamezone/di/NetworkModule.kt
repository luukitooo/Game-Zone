package com.lukaarmen.gamezone.di

import com.lukaarmen.data.remote.services.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttp(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(interceptor)
        }.build()
    }

    @Singleton
    @Provides
    fun provideLoggingInterceptor(): Interceptor {
        return Interceptor {
            val request = it.request().newBuilder()
                .addHeader(
                    "authorization",
                    "Bearer wUtbrgpo0yhWgQaLWWPKH8CDUQ4jrJNm68fgubRtj2x5mjOlC8Y"
                )
                .addHeader(
                    "Content-Type",
                    "application/json"
                )
                .addHeader(
                    "accept",
                    "application/json"
                )
            val actualRequest = request.build()
            it.proceed(actualRequest)
        }
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.pandascore.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideLeaguesService(retrofit: Retrofit): LeaguesService {
        return retrofit.create(LeaguesService::class.java)
    }

    @Provides
    @Singleton
    fun provideMatchesService(retrofit: Retrofit): MatchesService {
        return retrofit.create(MatchesService::class.java)
    }

    @Provides
    @Singleton
    fun provideTeamsService(retrofit: Retrofit): TeamsService {
        return retrofit.create(TeamsService::class.java)
    }
}