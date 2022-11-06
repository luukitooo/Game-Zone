package com.lukaarmen.gamezone.model

import com.lukaarmen.gamezone.common.base.adapter.Recyclable
import com.lukaarmen.gamezone.common.util.GameType

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