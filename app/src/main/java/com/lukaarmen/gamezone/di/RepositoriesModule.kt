package com.lukaarmen.gamezone.di

import com.lukaarmen.data.repository.*
import com.lukaarmen.domain.repository.*
import com.lukaarmen.domain.repository.firebase.ChatsRepository
import com.lukaarmen.domain.repository.firebase.UsersRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoriesModule {

    @Singleton
    @Binds
    fun bindLeaguesRepositoryImpl(repositoryImpl: LeaguesRepositoryImpl): LeaguesRepository

    @Singleton
    @Binds
    fun bindMatchesRepositoryImpl(repositoryImpl: MatchesRepositoryImpl): MatchesRepository

    @Singleton
    @Binds
    fun bindFavoriteLeaguesRepositoryImpl(favoritesRepositoryImpl: FavoriteLeaguesRepositoryImpl): FavoriteLeaguesRepository

    @Singleton
    @Binds
    fun bindTeamsRepositoryImpl(repositoryImpl: TeamsRepositoryImpl): TeamsRepository

    @Singleton
    @Binds
    fun bindUsersRepositoryImpl(repositoryImpl: UsersRepositoryImpl): UsersRepository

    @Singleton
    @Binds
    fun bindChatsRepositoryImpl(repositoryImpl: ChatsRepositoryImpl): ChatsRepository

    @Singleton
    @Binds
    fun provideNotificationsRepositoryImpl(repository: NotificationRepositoryImpl): NotificationRepository

}