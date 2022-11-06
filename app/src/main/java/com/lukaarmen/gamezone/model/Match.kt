package com.lukaarmen.gamezone.model

import com.lukaarmen.gamezone.common.base.adapter.Recyclable

data class Match(
    val beginAt: String?,
    val draw: Boolean?,
    val endAt: String?,
    val games: List<Game?>?,
    val id: Int?,
    val matchType: String?,
    val modifiedAt: String?,
    val name: String?,
    val numberOfGames: Int?,
    val opponents: List<Team?>?,
    val results: List<Result?>?,
    val slug: String?,
    val status: String?,
    val streamsList: List<Stream?>?,
    val videoGame: VideoGame?,
    val winner: Team?,
) : Recyclable<Match>() {

    override val inner: Match
        get() = this

    override val uniqueValue: Any
        get() = id!!

    override fun compareTo(other: Any?): Boolean {
        return other is Match && this == other
    }

    data class Game(
        val beginAt: String?,
        val complete: Boolean?,
        val endAt: String?,
        val finished: Boolean?,
        val id: Int?,
        val length: Int?,
        val matchId: Int?,
        val position: Int?,
        val status: String?,
        val winnerId: Int?,
    )

    data class Result(
        val score: Int?,
        val teamId: Int?
    )

    data class Stream(
        val embedUrl: String?,
        val language: String?,
        val main: Boolean?,
        val official: Boolean?,
        val rawUrl: String?
    )

}
