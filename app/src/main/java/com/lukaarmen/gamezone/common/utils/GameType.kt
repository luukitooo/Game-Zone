package com.lukaarmen.gamezone.common.utils

import com.lukaarmen.gamezone.R


enum class GameType(val title: String, val image: Int ) {
    ALL("all", R.drawable.img_all),
    CSGO("csgo", R.drawable.img_csgo_icon),
    DOTA2("dota2", R.drawable.img_dota2_icon),
    OWERWATCH("ow", R.drawable.img_overwatch_icon),
    RAINBOW_SIX("r6siege", R.drawable.img_rainbow_six_icon),
}

data class CategoryIndicator(
    val gameType: GameType,
    val isSelected: Boolean,
): Recyclable<CategoryIndicator>(){

    override val uniqueValue: Any = gameType.title
    override val inner: CategoryIndicator = this

    override fun compareTo(other: Any?): Boolean {
        return other is CategoryIndicator && this == other
    }

}