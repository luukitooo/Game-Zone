package com.lukaarmen.gamezone.common.base.adapter

abstract class Recyclable<T> {

    abstract val inner: T

    abstract val uniqueValue: Any

    abstract fun compareTo(other: Any?): Boolean

    override fun equals(other: Any?): Boolean {
        return compareTo(other)
    }

    override fun hashCode(): Int {
        var result = inner?.hashCode() ?: 0
        result = 31 * result + uniqueValue.hashCode()
        return result
    }

}