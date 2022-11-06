package com.lukaarmen.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lukaarmen.data.local.entity.FavoriteLeagueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteLeaguesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLeague(league: FavoriteLeagueEntity)

    @Delete
    suspend fun removeLeague(league: FavoriteLeagueEntity)

    @Query("SELECT * FROM favorite_leagues")
    fun getAsFlow(): Flow<List<FavoriteLeagueEntity>>

    @Query("SELECT * FROM favorite_leagues WHERE user_id = :uid")
    fun getAsFlowByUid(uid: String): Flow<List<FavoriteLeagueEntity>>

    @Query("SELECT * FROM favorite_leagues")
    suspend fun getAllLeagues(): List<FavoriteLeagueEntity>

    @Query("SELECT * FROM favorite_leagues WHERE user_id = :uid")
    suspend fun getLeaguesByUid(uid: String): List<FavoriteLeagueEntity>

    @Query("SELECT * FROM favorite_leagues WHERE league_id = :id")
    suspend fun getLeagueById(id: Int): FavoriteLeagueEntity

    @Query("SELECT * FROM favorite_leagues WHERE title LIKE '%' || :title || '%'")
    suspend fun getLeaguesByTitle(title: String): List<FavoriteLeagueEntity>

}