package com.lukaarmen.domain.model

data class LeaguesDomain(
    val id: Int?,
    val imageUrl: Any?,
    val modifiedAt: String?,
    val name: String?,
    val slug: String?,
    val url: String?,
    val videoGame: VideoGameDomain?
)