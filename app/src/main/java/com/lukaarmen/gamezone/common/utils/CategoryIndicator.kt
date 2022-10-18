package com.lukaarmen.gamezone.common.utils

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

val gamesList = mutableListOf(
    CategoryIndicator(GameType.ALL, true),
    CategoryIndicator(GameType.CSGO, false),
    CategoryIndicator(GameType.DOTA2, false),
    CategoryIndicator(GameType.OWERWATCH, false),
    CategoryIndicator(GameType.RAINBOW_SIX, false),
)