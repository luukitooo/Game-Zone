package com.lukaarmen.gamezone.common.util

import java.time.LocalDateTime

object ActivityStatus {

    const val IS_ACTIVE = "Is Active"

    fun generateWasActive(): String {
        val time = LocalDateTime.now()
        val formatted ="${time.hour}:${time.minute} ${time.dayOfMonth}/${time.monthValue}/${time.year}"
        return "was active at: $formatted"
    }

}