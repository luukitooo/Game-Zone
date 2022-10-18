package com.lukaarmen.domain.models

data class TeamDomain(
    val acronym: Any?,
    val currentVideoGame: VideoGameDomain?,
    val id: Int?,
    val imageUrl: String?,
    val location: String?,
    val modifiedAt: String?,
    val name: String?,
    val players: List<PlayerDomain?>?,
    val slug: String?
) {

    data class PlayerDomain(
        val age: Int?,
        val birthday: Any?,
        val firstName: String?,
        val id: Int?,
        val imageUrl: Any?,
        val lastName: String?,
        val name: String?,
        val nationality: String?,
        val role: Any?,
        val slug: String?
    )
}
