package com.lukaarmen.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lukaarmen.domain.model.FavoriteLeagueDomain

@Entity(tableName = "favorite_leagues")
data class FavoriteLeagueEntity(

    @PrimaryKey
    @ColumnInfo(name = "league_id")
    val leagueId: Int,

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String,

    @ColumnInfo(name = "game_type")
    val gameType: String

) {

    fun toFavoriteLeagueDomain() = FavoriteLeagueDomain(
        leagueId = leagueId,
        userId = userId,
        title = title,
        imageUrl = imageUrl,
        gameType = gameType
    )

    companion object {

        fun fromFavoriteLeagueDomain(
            favoriteLeagueDomain: FavoriteLeagueDomain
        ) = FavoriteLeagueEntity(
            leagueId = favoriteLeagueDomain.leagueId,
            userId = favoriteLeagueDomain.userId,
            title = favoriteLeagueDomain.title,
            imageUrl = favoriteLeagueDomain.imageUrl,
            gameType = favoriteLeagueDomain.gameType
        )


    }

}