package com.lukaarmen.domain.models

data class SeriesDomain(
    val beginAt: String?,
    val endAt: Any?,
    val fullName: String?,
    val id: Int?,
    val leagueId: Int?,
    val modifiedAt: String?,
    val name: String?,
    val season: String?,
    val slug: String?,
    val tournaments: List<TournamentsDomain?>?,
    val videoGame: VideoGameDomain?,
    val year: Int?
)