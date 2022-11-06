package com.lukaarmen.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NotificationBodyDto(
    val to: String?,
    val data: DataDto?
) {
    data class DataDto(
        val title: String?,
        val message: String?,
        @SerializedName("sender_image")
        val senderImage: String?
    )
}
