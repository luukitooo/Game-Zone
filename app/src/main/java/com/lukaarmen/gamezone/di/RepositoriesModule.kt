package com.lukaarmen.gamezone.di

import com.lukaarmen.data.repositories.LeaguesRepositoryImpl
import com.lukaarmen.data.repositories.MatchesRepositoryImpl
import com.lukaarmen.data.repositories.SeriesRepositoryImpl
import com.lukaarmen.data.repositories.TournamentRepositoryImpl
import com.lukaarmen.domain.repositories.LeaguesRepository
import com.lukaarmen.domain.repositories.MatchesRepository
import com.lukaarmen.domain.repositories.SeriesRepository
import com.lukaarmen.domain.repositories.TournamentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoriesModule {

    @Singleton
    @Binds
    abstract fun bindLeaguesRepositoryImpl(repositoryImpl: LeaguesRepositoryImpl): LeaguesRepository

    @Singleton
    @Binds
    abstract fun bindSeriesRepositoryImpl(repositoryImpl: SeriesRepositoryImpl): SeriesRepository

    @Singleton
    @Binds
    abstract fun bindTournamentsRepositoryImpl(repositoryImpl: TournamentRepositoryImpl): TournamentRepository

    @Singleton
    @Binds
    abstract fun bindMatchesRepositoryImpl(repositoryImpl: MatchesRepositoryImpl): MatchesRepository

}