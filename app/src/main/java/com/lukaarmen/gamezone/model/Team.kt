package com.lukaarmen.gamezone.model

data class Team(
    val acronym: Any?,
    val currentVideoGame: VideoGame?,
    val id: Int?,
    val imageUrl: String?,
    val location: String?,
    val modifiedAt: String?,
    val name: String?,
    val players: List<Player?>?,
    val slug: String?
) {

    data class Player(
        val age: Int?,
        val birthday: Any?,
        val firstName: String?,
        val id: Int?,
        val imageUrl: String?,
        val lastName: String?,
        val name: String?,
        val nationality: String?,
        val role: Any?,
        val slug: String?
    )
}
