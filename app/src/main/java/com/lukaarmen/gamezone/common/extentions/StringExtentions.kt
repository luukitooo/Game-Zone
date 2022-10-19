package com.lukaarmen.gamezone.common.extentions

fun String.getStreamPreview(quality: String): String{
    val list = this.split("=")
    val link = "https://static-cdn.jtvnw.net/previews-ttv/live_user_${list.last()}-$quality.jpg"
    return link
}

