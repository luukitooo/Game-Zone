package com.lukaarmen.data.remote.dto


import com.google.gson.annotations.SerializedName

data class TeamDto(
    val acronym: Any?,
    @SerializedName("current_videogame")
    val currentVideoGame: VideoGameDto?,
    val id: Int?,
    @SerializedName("image_url")
    val imageUrl: String?,
    val location: String?,
    @SerializedName("modified_at")
    val modifiedAt: String?,
    val name: String?,
    val players: List<PlayerDto?>?,
    val slug: String?
) {

    data class PlayerDto(
        val age: Int?,
        val birthday: Any?,
        @SerializedName("first_name")
        val firstName: String?,
        val id: Int?,
        @SerializedName("image_url")
        val imageUrl: String?,
        @SerializedName("last_name")
        val lastName: String?,
        val name: String?,
        val nationality: String?,
        val role: Any?,
        val slug: String?
    )
}