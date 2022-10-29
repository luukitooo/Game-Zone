package com.lukaarmen.gamezone.di

import com.lukaarmen.data.repositories.*
import com.lukaarmen.domain.repositories.*
import com.lukaarmen.domain.repositories.firebase.UsersRepository
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

    @Singleton
    @Binds
    abstract fun bindFavoriteLeaguesRepositoryImpl(favoritesRepositoryImpl: FavoriteLeaguesRepositoryImpl): FavoriteLeaguesRepository

    @Singleton
    @Binds
    abstract fun bindTeamsRepositoryImpl(repositoryImpl: TeamsRepositoryImpl): TeamsRepository

    @Singleton
    @Binds
    abstract fun bindUsersRepositoryImpl(repositoryImpl: UsersRepositoryImpl): UsersRepository

}