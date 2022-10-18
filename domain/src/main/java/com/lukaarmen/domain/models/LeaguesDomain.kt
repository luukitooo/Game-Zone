package com.lukaarmen.domain.models

data class LeaguesDomain(
    val id: Int?,
    val imageUrl: Any?,
    val modifiedAt: String?,
    val name: String?,
    val series: List<SeriesDomain?>?,
    val slug: String?,
    val url: String?,
    val videoGame: VideoGameDomain?
)