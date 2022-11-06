package com.lukaarmen.gamezone.model

import com.lukaarmen.domain.model.FavoriteLeagueDomain
import com.lukaarmen.gamezone.common.base.adapter.Recyclable

data class FavoriteLeague(
    val leagueId: Int,
    val userId: String,
    val title: String,
    val imageUrl: String,
    val gameType: String
): Recyclable<FavoriteLeague>() {

    fun toFavoriteLeagueDomain() = FavoriteLeagueDomain(
        leagueId = leagueId,
        userId = userId,
        title = title,
        imageUrl = imageUrl,
        gameType = gameType
    )

    companion object {
        fun fromFavoriteLeagueDomain(league: FavoriteLeagueDomain) = FavoriteLeague(
            leagueId = league.leagueId,
            userId = league.userId,
            imageUrl = league.imageUrl,
            title = league.title,
            gameType = league.gameType
        )
    }

    override val inner: FavoriteLeague
        get() = this

    override val uniqueValue: Any
        get() = leagueId

    override fun compareTo(other: Any?): Boolean {
        return other is FavoriteLeague && this == other
    }

}
