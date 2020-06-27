package com.example.flightmobileapp

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.net.URL

class ControlManager(private var context: Context) : AppCompatActivity() {

    private lateinit var url: URL
    private lateinit var connection: HttpURLConnection
    private var aileron: Double = 0.0
    private var elevator: Double = 0.0
    private var throttle: Double = 0.0
    private var rudder: Double = 0.0
    private lateinit var api: Api


    fun setAileron(value: Double) {
        aileron = value
    }

    fun setElevator(value: Double) {
        elevator = value
    }

    fun setThrottle(value: Double) {
        throttle = value
    }

    fun setRudder(value: Double) {
        rudder = value
    }

    fun getThrottle(): Double {
        return throttle
    }

    fun getRudder(): Double {
        return rudder
    }


    fun connect(u: String):Boolean {
        val temp: URL
        try {
            temp = URL(u)
            connection = temp.openConnection() as HttpURLConnection
        } catch (e: Exception) {
            return false
        }
        url = temp
        return true
    }


    fun sendCommand(): Boolean {
        var succeed = false;
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.setRequestProperty("Accept", "application/json")
        connection.doOutput = true
        // create json command
        val newCommand =
            Command(rudder.toFloat(), elevator.toFloat(), aileron.toFloat(), throttle.toFloat())
        api.post(newCommand).enqueue(object : Callback<Command> {
            override fun onFailure(call: Call<Command>, t: Throwable) {
                setNotification("server isn't responding")
            }
            override fun onResponse(call: Call<Command>, response: Response<Command>) {
                if (response.code() == 200) {
                    succeed = true
                }
                else if(response.code() == 500) {
                    setNotification("connection failed")
                } else if(response.code() == 400){
                    setNotification("format error")
                } else {
                    setNotification("error")
                }
            }
        })
        return succeed
    }


    fun setNotification(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    fun shouldSend(newVal: Double, lastVal: Double): Boolean {
        // checks if values changed in more than 1%
        if ((newVal > 1.01 * lastVal) || (newVal < 0.99 * lastVal)) {
            return true
        }
        return false
    }

    fun getImage(image: ImageView) {
        api?.getScreenshot()?.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 500) {
                    setNotification("connection failed")
                } else if (response.code() == 400) {
                    setNotification("format error")
                } else if (response.code() == 200) {
                    val i = response?.body()?.byteStream()
                    val b = BitmapFactory.decodeStream(i)
                    runOnUiThread { image.setImageBitmap(b) }
                } else {
                    setNotification("error in getting the image")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                setNotification("timeout - server not responding")
            }
        })
    }

}