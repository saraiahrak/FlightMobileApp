package com.example.flightmobileapp

import android.content.Context
import android.widget.Toast
import com.google.firebase.database.*

class DataCache(cap: Int, context: Context) {

    var data: Array<String?> = arrayOfNulls<String>(cap)
    var capacity: Int = 0
    var physize: Int = 0
    private val applicationContext = context

    init {
        capacity = cap
//        val ref = FirebaseDatabase.getInstance().getReference("URLs")
//        ref.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                var list: Array<UserURL?> = arrayOfNulls(5)
//                for (snap in p0.children) {
//                    var map = snap.value as HashMap<String, String>
//                    var position = map["id"]!!.toInt()
//                    var url = map["url"]
//                    data[position] = url
//                    physize++
//                }
//            }
//        })
    }

    public fun isEmpty(): Boolean {
        return physize == 0
    }

//    private fun initialize() {
//        val ref = FirebaseDatabase.getInstance().getReference("URLs")
//        ref.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                var list: Array<UserURL?> = arrayOfNulls(5)
//                for (snap in p0.children) {
//                    var map = snap.value as HashMap<String, String>
//                    var position = map["id"]!!.toInt()
//                    var url = map["url"]
//                    data[position] = url
//                    physize++
//                }
//            }
//        })
//
//        ref.addValueEventListener(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                if (p0!!.exists()) {
//                    for (child in p0.children) {
//                        val url = child.getValue(UserURL::class.java)
//                        var position = url!!.id.toInt()
//                        data[position!!] = url.url
//                    }
//                }
//            }
//        })
//    }

    public fun insert(url: String) {
        if (url.isNullOrEmpty()) {
            Toast.makeText(
                applicationContext,
                "Please insert URL to connect",
                Toast.LENGTH_LONG
            ).show()
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
            if (current == null) {
                break
            }

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