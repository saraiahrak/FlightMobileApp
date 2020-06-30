package com.example.flightmobileapp

import androidx.room.*

@Dao
interface URLDao {

    @Query("SELECT * FROM urls")
    fun getAll(): Array<UserURL>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(urlList: List<UserURL?>)

    @Query("DELETE FROM urls")
    fun deleteAll()

    @Delete
    fun delete(userURL: UserURL)

    @Insert
    fun insertAll(vararg users: UserURL)
}