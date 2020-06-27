package com.example.flightmobileapp


data class UserURL(val id: String, val url: String) {

    fun isEqual(other: UserURL): Boolean {
        return this.url == other.url
    }

}