package com.lukaarmen.gamezone.model

import com.lukaarmen.gamezone.common.base.adapter.Recyclable

data class League(
    val id: Int?,
    val imageUrl: Any?,
    val name: String?,
    val url: String?,
    var isSaved: Boolean = false
) : Recyclable<League>() {

    override val inner: League
        get() = this

    override val uniqueValue: Any
        get() = id ?: -1

    override fun compareTo(other: Any?): Boolean {
        return other is League && this == other
    }

    fun toFavoriteLeague(uid: String, gameType: String) = FavoriteLeague(
        leagueId = this.id!!,
        userId = uid,
        title = this.name!!,
        imageUrl = try {
            this.imageUrl as String
        } catch (t: Throwable) { "" },
        gameType = gameType
    )

}