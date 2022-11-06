package com.lukaarmen.domain.model

data class MatchDomain(
    val beginAt: String?,
    val draw: Boolean?,
    val endAt: String?,
    val games: List<GameDomain?>?,
    val id: Int?,
    val matchType: String?,
    val modifiedAt: String?,
    val name: String?,
    val numberOfGames: Int?,
    var opponents: List<TeamDomain?>?,
    val results: List<ResultDomain?>?,
    val slug: String?,
    val status: String?,
    val streamsList: List<StreamDomain?>?,
    val videoGame: VideoGameDomain?,
    val winner: TeamDomain?,
) {

//    override val inner: MatchDomain
//        get() = this
//
//    override val uniqueValue: Any
//        get() = id!!
//
//    override fun compareTo(other: Any?): Boolean {
//        return other is MatchDomain && this == other
//    }

    data class GameDomain(
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

    data class ResultDomain(
        val score: Int?,
        val teamId: Int?
    )

    data class StreamDomain(
        val embedUrl: String?,
        val language: String?,
        val main: Boolean?,
        val official: Boolean?,
        val rawUrl: String?
    )


}