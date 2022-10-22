package com.lukaarmen.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lukaarmen.data.local.dao.FavoriteLeaguesDao
import com.lukaarmen.data.local.model.FavoriteLeagueEntity

@Database(entities = [FavoriteLeagueEntity::class], version = 1)
abstract class GameZoneDatabase: RoomDatabase() {

    abstract val favoriteLeaguesDao: FavoriteLeaguesDao

}