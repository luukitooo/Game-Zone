package com.lukaarmen.data.remote.dto


import com.google.gson.annotations.SerializedName

data class TournamentDto(
    @SerializedName("begin_at")
    val beginAt: String?,
    @SerializedName("end_at")
    val endAt: Any?,
    val id: Int?,
    @SerializedName("live_supported")
    val liveSupported: Boolean?,
    val matches: List<MatchDto?>?,
    @SerializedName("modified_at")
    val modifiedAt: String?,
    val name: String?,
    val prizepool: Any?,
    val slug: String?,
    val teams: List<TeamDto?>?,
    @SerializedName("tier")
    val tier: String?,
    @SerializedName("videogame")
    val videoGame: VideoGameDto?,
    @SerializedName("winner_id")
    val winnerId: Any?,
    @SerializedName("winner_type")
    val winnerType: String?
)