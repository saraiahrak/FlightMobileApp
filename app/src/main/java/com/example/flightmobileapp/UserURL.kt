package com.example.flightmobileapp

import com.google.firebase.database.DataSnapshot

class UserURL(val id: String, val url: String) {

    public fun isEqual(other: UserURL): Boolean {
        return this.url == other.url
    }

}