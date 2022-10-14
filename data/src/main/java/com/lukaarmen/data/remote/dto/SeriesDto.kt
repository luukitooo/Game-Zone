package com.lukaarmen.data.remote.dto


import com.google.gson.annotations.SerializedName

data class SeriesDto(
    @SerializedName("begin_at")
    val beginAt: String?,
    @SerializedName("end_at")
    val endAt: Any?,
    @SerializedName("full_name")
    val fullName: String?,
    val id: Int?,
    @SerializedName("league_id")
    val leagueId: Int?,
    @SerializedName("modified_at")
    val modifiedAt: String?,
    val name: String?,
    val season: String?,
    val slug: String?,
    val tournaments: List<TournamentDto?>?,
    @SerializedName("videogame")
    val videoGame: VideoGameDto?,
    @SerializedName("videogame_title")
    val videoGameTitle: Any?,
    @SerializedName("winner_id")
    val winnerId: Any?,
    @SerializedName("winner_type")
    val winnerType: Any?,
    val year: Int?
)