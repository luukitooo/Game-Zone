package com.lukaarmen.data.remote.dto


import com.google.gson.annotations.SerializedName

data class LeaguesDto(
    val id: Int?,
    @SerializedName("image_url")
    val imageUrl: Any?,
    @SerializedName("modified_at")
    val modifiedAt: String?,
    val name: String?,
    val slug: String?,
    val url: String?,
    @SerializedName("videogame")
    val videoGame: VideoGameDto?
)