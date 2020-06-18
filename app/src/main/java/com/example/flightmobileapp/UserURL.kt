package com.example.flightmobileapp

class UserURL(val id: String, val url: String) {

    public fun isEqual(other: UserURL): Boolean {
        return this.url == other.url
    }

}