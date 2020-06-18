package com.example.flightmobileapp

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class DataCache(cap: Int, context: Context) {

    var data: Array<String?> = arrayOfNulls<String>(cap)
    var capacity: Int = 0
    var physize: Int = 0;
    private val applicationContext = context

    init {
        capacity = cap
    }

    public fun insert(url: String) {
        if (url.isNullOrEmpty()) {
            return
        }
        if (exists(url)) {
            moveToFront(url)
        } else {
            setPhysize()
            insertNew(url)
        }
        saveAll()
    }


    private fun saveAll() {
        val ref = FirebaseDatabase.getInstance().getReference("URLs")
        var i = 0
        while (i < physize) {
            var current = data[i]
            if (current == null)
                break
            val obj = UserURL(i.toString(), current)
            ref.child(i.toString()).setValue(obj).addOnCompleteListener {
                Toast.makeText(applicationContext, "Saved!", Toast.LENGTH_LONG).show()
            }
            i++
        }
    }


    private fun setPhysize() {
        if (!isFull()) {
            physize++
        }
    }

    private fun exists(url: String): Boolean {
        return data.contains(url)
    }

    private fun moveToFront(url: String) {
        val position = findPos(url)
        moveByOne(position)
        data[0] = url
    }

    private fun insertNew(url: String) {
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

    private fun findPos(url: String): Int {
        var i: Int = 0

        while (i < physize) {
            if (data[i] == url) {
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