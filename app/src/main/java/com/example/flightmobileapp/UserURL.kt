package com.example.flightmobileapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "urls")
data class UserURL(@PrimaryKey val url: String) {

    fun isEqual(other: UserURL): Boolean {
        return this.url == other.url
    }

}