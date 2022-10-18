package com.lukaarmen.gamezone.common.extentions

fun String.getStreamPreview(): String{
    val list = this.split("=")
    val link = "https://static-cdn.jtvnw.net/previews-ttv/live_user_${list.last()}-250x150.jpg"
    return link
}