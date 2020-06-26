package com.example.flightmobileapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

data class UserURL(val id: String, val url: String) {

    public fun isEqual(other: UserURL): Boolean {
        return this.url == other.url
    }

}