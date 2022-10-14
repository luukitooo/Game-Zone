package com.lukaarmen.data.remote.dto


import com.google.gson.annotations.SerializedName

data class MatchDto(
    @SerializedName("begin_at")
    val beginAt: String?,
    @SerializedName("detailed_stats")
    val detailedStats: Boolean?,
    val draw: Boolean?,
    @SerializedName("end_at")
    val endAt: String?,
    val forfeit: Boolean?,
    @SerializedName("game_advantage")
    val gameAdvantage: Any?,
    val games: List<GameDto?>?,
    val id: Int?,
    @SerializedName("match_type")
    val matchType: String?,
    @SerializedName("modified_at")
    val modifiedAt: String?,
    val name: String?,
    @SerializedName("number_of_games")
    val numberOfGames: Int?,
    val opponents: List<Opponent?>?,
    @SerializedName("original_scheduled_at")
    val originalScheduledAt: String?,
    val rescheduled: Boolean?,
    val results: List<ResultDto?>?,
    @SerializedName("scheduled_at")
    val scheduledAt: String?,
    val slug: String?,
    val status: String?,
    @SerializedName("streams_list")
    val streamsList: List<StreamDto?>?,
    val videogame: VideoGameDto?,
    @SerializedName("videogame_version")
    val winner: TeamDto?,
    @SerializedName("winner_id")
    val winnerId: Int?,
    @SerializedName("winner_type")
    val winnerType: String?
) {
    data class GameDto(
        @SerializedName("begin_at")
        val beginAt: String?,
        val complete: Boolean?,
        @SerializedName("detailed_stats")
        val detailedStats: Boolean?,
        @SerializedName("end_at")
        val endAt: String?,
        val finished: Boolean?,
        val forfeit: Boolean?,
        val id: Int?,
        val length: Int?,
        @SerializedName("match_id")
        val matchId: Int?,
        val position: Int?,
        val status: String?,
        val winner: Winner?,
        @SerializedName("winner_type")
        val winnerType: String?
    ) {
        data class Winner(
            val id: Int?,
            val type: String?
        )
    }

    data class Opponent(
        val opponent: TeamDto?,
        val type: String?
    )

    data class ResultDto(
        val score: Int?,
        @SerializedName("team_id")
        val teamId: Int?
    )

    data class StreamDto(
        @SerializedName("embed_url")
        val embedUrl: String?,
        val language: String?,
        val main: Boolean?,
        val official: Boolean?,
        @SerializedName("raw_url")
        val rawUrl: String?
    )
}