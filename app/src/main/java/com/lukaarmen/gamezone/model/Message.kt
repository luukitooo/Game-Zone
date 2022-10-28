package com.lukaarmen.gamezone.model

class Message() {

    var id: String = ""
    var senderId: String? = null
    var recipientId: String? = null
    var type: String? = null
    var text: String = ""
    var imageUrl: String = ""

    constructor(
        id: String = "",
        senderId: String,
        recipientId: String,
        type: String,
        text: String = "",
        imageUrl: String = ""
    ): this() {
        this.id = id
        this.senderId = senderId
        this.recipientId = recipientId
        this.type = type
        this.text = text
        this.imageUrl = imageUrl
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Message

        if (senderId != other.senderId) return false
        if (recipientId != other.recipientId) return false
        if (type != other.type) return false
        if (text != other.text) return false
        if (imageUrl != other.imageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = senderId.hashCode()
        result = 31 * result + recipientId.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + text.hashCode()
        result = 31 * result + imageUrl.hashCode()
        return result
    }


}