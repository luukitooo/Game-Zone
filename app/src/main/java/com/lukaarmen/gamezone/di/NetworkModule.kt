package com.lukaarmen.gamezone.di

import android.content.Context
import com.lukaarmen.data.remote.service.*
import com.lukaarmen.gamezone.common.extension.isNetworkConnection
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpCache(
        @Named("OnlineInterceptor") onlineInterceptor: Interceptor,
        @Named("OffLineInterceptor") offlineInterceptor: Interceptor,
        @Named("LoggingInterceptor") loggingInterceptor: Interceptor,
        cache: Cache
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(offlineInterceptor)
            addNetworkInterceptor(onlineInterceptor)
            addInterceptor(loggingInterceptor)
            cache(cache)
        }.build()
    }

    @Singleton
    @Provides
    @Named("LoggingInterceptor")
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

    @Singleton
    @Provides
    @Named("OnlineInterceptor")
    fun provideOnlineInterceptor(): Interceptor = Interceptor { chain ->
        val response = chain.proceed(chain.request())
        val maxAge = 60
        response.newBuilder()
            .header("Cache-Control", "public, max-age=$maxAge")
            .removeHeader("Pragma")
            .build()
    }

    @Provides
    @Singleton
    @Named("OffLineInterceptor")
    fun provideOffLineInterceptor(@ApplicationContext context: Context): Interceptor =
        Interceptor { chain ->
            var request: Request = chain.request()
            if (!context.isNetworkConnection()) {
                val maxStale = 60 * 60 * 24 * 30
                request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=$maxStale")
                    .removeHeader("Pragma")
                    .build()
            }
            chain.proceed(request)
        }

    @Provides
    @Singleton
    fun providesCache(@ApplicationContext context: Context): Cache {
        return Cache(context.cacheDir, (10 * 1024 * 1024).toLong())
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