package com.lukaarmen.gamezone.models

import com.lukaarmen.gamezone.common.utils.Recyclable

data class MatchPlayers(
    val firstTeamPlayer: Team.Player? = null,
    var secondTeamPlayer: Team.Player? = null
) : Recyclable<MatchPlayers>() {

    override val inner: MatchPlayers
        get() = this

    override val uniqueValue: Any
        get() = firstTeamPlayer!!

    override fun compareTo(other: Any?): Boolean {
        return other is Match && this == other
    }

}