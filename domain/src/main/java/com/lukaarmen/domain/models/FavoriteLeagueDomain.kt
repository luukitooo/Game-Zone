package com.lukaarmen.domain.models

data class FavoriteLeagueDomain(
    val leagueId: Int,
    val userId: String,
    val title: String,
    val imageUrl: String,
    val gameType: String
)