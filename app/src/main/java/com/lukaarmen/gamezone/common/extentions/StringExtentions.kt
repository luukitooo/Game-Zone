package com.lukaarmen.gamezone.common.extentions

import android.view.View
import androidx.appcompat.widget.AppCompatTextView

fun String.getStreamPreview(quality: String): String {
    val list = this.split("=")
    val link = "https://static-cdn.jtvnw.net/previews-ttv/live_user_${list.last()}-$quality.jpg"
    return link
}

fun String.filterDate() = this.dropLast(4).replace("T", " ")





