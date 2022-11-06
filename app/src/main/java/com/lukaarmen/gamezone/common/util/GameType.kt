package com.lukaarmen.gamezone.common.util

import com.lukaarmen.gamezone.R

enum class GameType(val title: String, val image: Int,  val gameWallpaper: Int) {
    ALL("all", R.drawable.img_all, 0),
    CSGO("csgo", R.drawable.img_csgo_icon, R.drawable.img_csgo_bg),
    DOTA2("dota2", R.drawable.img_dota2_icon, R.drawable.img_dota2_bg),
    OWERWATCH("ow", R.drawable.img_overwatch_icon, R.drawable.img_overwatch_bg),
    RAINBOW_SIX("r6siege", R.drawable.img_rainbow_six_icon, R.drawable.imp_rainbow_six_bg),
}