package com.lukaarmen.domain.models

data class TournamentsDomain(
    val beginAt: String?,
    val endAt: Any?,
    val id: Int?,
    val matches: List<MatchDomain?>?,
    val modifiedAt: String?,
    val name: String?,
    val prizepool: Any?,
    val slug: String?,
    val teams: List<TeamDomain?>?,
    val tier: String?,
    val videoGame: VideoGameDomain?
)
