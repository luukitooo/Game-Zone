package com.lukaarmen.gamezone.common.utils

data class ViewState<T>(
    val data: T? = null,
    val error: String? = null,
    val isLoading: Boolean? = null
)