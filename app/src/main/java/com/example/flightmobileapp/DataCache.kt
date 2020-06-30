package com.example.flightmobileapp

import android.content.Context
import android.widget.Toast

class DataCache(cap: Int, context: Context) {

    var db = UserRoomDatabase.getInstance(context)
    var dao: URLDao
    var data: Array<UserURL?> = arrayOfNulls<UserURL>(cap)
    var capacity: Int = 0
    var physize: Int = 0
    private lateinit var list: List<UserURL>
    private val applicationContext = context

    init {
        capacity = cap
        dao = db.urlDao()
    }

    fun isEmpty(): Boolean {
        return physize == 0
    }


    fun insert(url: UserURL, isFirst: Boolean) {
        if (url.url.isNullOrEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please insert URL to connect",
                Toast.LENGTH_LONG
            ).show()
            return
        }
        if (exists(url)) {
            moveToFront(url)
            dao.deleteAll()
            for (item: UserURL? in data) {
                if (item != null) {
                    dao.insertAll(item)
                }
//                dao.insertAll(item!!)
            }
        } else {
            setPhysize()
            insertNew(url)
            if (!isFirst) {
                dao.insertAll(url)
            }
        }
//        saveAll()
    }

//
//    private fun saveAll() {
//        var i = 0
//        while (i < physize) {
//            var current = data[i]
//            if (current == null) {
//                break
//            }
//
//            val obj = UserURL(current)
//            //insert to room dao
//            i++
//        }
//    }


    private fun setPhysize() {
        if (!isFull()) {
            physize++
        }
    }

    private fun exists(url: UserURL): Boolean {

        for (item in data) {
            if (item != null && url.isEqual(item)) {
                return true
            }
        }
        return false
//        return data.contains(url.url)
    }

    private fun moveToFront(url: UserURL) {
        val position = findPos(url)
        moveByOne(position)
        data[0] = url
    }

    private fun insertNew(url: UserURL) {
        val i: Int;
        if (isFull()) {
            i = physize - 1
        } else {
            i = physize
        }
        moveByOne(i)
        data[0] = url
    }

    private fun moveByOne(position: Int) {
        var i: Int = position
        while (i > 0) {
            data[i] = data[i - 1]
            i--
        }
    }

    private fun findPos(url: UserURL): Int {
        var i: Int = 0

        while (i < physize) {
            if (data[i]?.isEqual(url)!!) {
                return i
            }
            i++
        }
        return physize
    }

    private fun isFull(): Boolean {
        return physize == capacity
    }
}