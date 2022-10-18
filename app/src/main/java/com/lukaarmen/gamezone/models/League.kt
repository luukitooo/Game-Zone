package com.lukaarmen.gamezone.models

import com.lukaarmen.gamezone.common.utils.Recyclable

data class League(
    val id: Int?,
    val imageUrl: Any?,
    val name: String?,
    val url: String?
) : Recyclable<League>() {

    override val inner: League
        get() = this

    override val uniqueValue: Any
        get() = id ?: -1

    override fun compareTo(other: Any?): Boolean {
        return other is League && this == other
    }
}