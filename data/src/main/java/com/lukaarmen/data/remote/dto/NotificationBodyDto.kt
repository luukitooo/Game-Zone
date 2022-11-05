package com.lukaarmen.data.remote.dto

data class NotificationBodyDto(
    val to: String?,
    val data: DataDto?
) {
    data class DataDto(
        val title: String?,
        val message: String?,
        val sender_image: String?
    )
}
