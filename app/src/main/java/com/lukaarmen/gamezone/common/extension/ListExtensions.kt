package com.lukaarmen.gamezone.common.extension

import com.lukaarmen.gamezone.common.util.Quality
import com.lukaarmen.gamezone.model.Match

fun List<Match.Stream?>?.checkForPreview(): String? {
    this?.let {
        return if (this.isNotEmpty()) this.last()?.embedUrl?.getStreamPreview(Quality.LOW)
        else null
    }
    return null
}