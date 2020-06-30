package com.example.flightmobileapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = arrayOf(UserURL::class), version = 1, exportSchema = false)
public abstract class UserRoomDatabase : RoomDatabase() {
    abstract fun urlDao(): URLDao

    companion object {
        private var instance: UserRoomDatabase? = null

        fun getInstance(context: Context): UserRoomDatabase {
            val temp = instance
            if (temp != null) {
                return temp
            }

            synchronized(this) {
                val current = Room.databaseBuilder(
                    context.applicationContext,
                    UserRoomDatabase::class.java,
                    "url_db"
                ).allowMainThreadQueries().build()
                instance = current
                return current
            }
        }
    }

}