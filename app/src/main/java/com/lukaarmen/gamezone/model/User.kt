package com.lukaarmen.gamezone.model

class User() {

    var uid: String? = null
    var email: String? = null
    var username: String? = null
    var imageUrl: String? = null

    constructor(
        uid: String,
        email: String,
        username: String,
        imageUrl: String = "",
    ) : this() {
        this.uid = uid
        this.email = email
        this.username = username
        this.imageUrl = imageUrl
    }

    override fun toString(): String {
        return "User(uid='$uid', email='$email', username='$username', imageUrl='$imageUrl')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (uid != other.uid) return false
        if (email != other.email) return false
        if (username != other.username) return false
        if (imageUrl != other.imageUrl) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid.hashCode()
        result = 31 * result + email.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + imageUrl.hashCode()
        return result
    }

}