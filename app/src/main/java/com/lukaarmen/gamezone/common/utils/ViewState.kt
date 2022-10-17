package com.lukaarmen.gamezone.common.utils

data class ViewState<T>(
    val data: T? = null,
    val error: String? = "",
    val isLoading: Boolean? = false
)