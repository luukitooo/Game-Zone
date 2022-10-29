package com.lukaarmen.gamezone.model

data class Message(
    var id: String = "",
    var senderId: String? = null,
    var recipientId: String? = null,
    var type: String? = null,
    var text: String = "",
    var imageUrl: String = "",
)