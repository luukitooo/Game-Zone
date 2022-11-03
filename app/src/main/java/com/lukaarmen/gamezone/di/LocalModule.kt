package com.lukaarmen.gamezone.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.lukaarmen.data.local.GameZoneDatabase
import com.lukaarmen.data.local.dao.FavoriteLeaguesDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {

    @Provides
    @Singleton
    fun provideGameZoneDatabase(
        application: Application
    ): GameZoneDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            GameZoneDatabase::class.java,
            "GameZoneDatabase"
        ).build()
    }

    @Provides
    @Singleton
    fun provideFavoriteLeaguesDao(
        database: GameZoneDatabase
    ): FavoriteLeaguesDao {
        return database.favoriteLeaguesDao
    }

}