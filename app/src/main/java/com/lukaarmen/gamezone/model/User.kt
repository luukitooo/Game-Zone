package com.lukaarmen.gamezone.model

data class User(
    val uid: String,
    val email: String,
    val username: String,
    val imageUrl: String = "",
)