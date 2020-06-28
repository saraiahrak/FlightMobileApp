package com.example.flightmobileapp

import android.content.Context
import android.graphics.BitmapFactory
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import okhttp3.MediaType
import okhttp3.RequestBody
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
    private var api: Api = RetrofitBuilder.getApi()

    private var lastAileronVal = 0.0
    private var lastElevatorVal = 0.0
    private var lastThrottleVal = 0.0
    private var lastRudderVal = 0.0
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


    fun shouldSendCommand(
        rudderVal: Double,
        elevatorVal: Double,
        aileronVal: Double,
        throttleVal: Double
    ): Boolean {
        if (shouldSend(rudderVal, lastRudderVal)) {
            lastRudderVal = rudderVal
            return true
        }
        if (shouldSend(elevatorVal, lastElevatorVal)) {
            lastElevatorVal = elevatorVal
            return true
        }
        if (shouldSend(aileronVal, lastAileronVal)) {
            lastAileronVal = aileronVal
            return true
        }
        if (shouldSend(throttleVal, lastThrottleVal)) {
            lastThrottleVal = throttleVal
            return true
        }
        return false
    }

    fun connect(u: String): Boolean {
        val temp: URL
        try {
//            temp = URL(u)

            temp = URL(u);
            connection = temp.openConnection() as HttpURLConnection
        } catch (e: Exception) {
            return false
        }
        url = temp
        return true
    }


    fun sendCommand(): Boolean {
        var succeed = false;
     /*   connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.setRequestProperty("Accept", "application/json")
        connection.doOutput = true*/
        // create json command

        val json =
            "{\"aileron\":$lastAileronVal,\n\"rudder\":$lastRudderVal,\n" +
                    "\"elevator\":$lastElevatorVal,\n\"throttle\":$lastThrottleVal\n}"
        val rb: RequestBody = RequestBody.create(MediaType.parse("application/json"), json)
/*        val newCommand =
            Command(
                lastRudderVal.toFloat(),
                lastElevatorVal.toFloat(),
                lastAileronVal.toFloat(),
                lastThrottleVal.toFloat()
            )*/
        api?.post(rb).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    succeed = true
                } else if (response.code() == 500) {
                    setNotification("connection failed")
                } else if (response.code() == 400) {
                    setNotification("format error")
                } else {
                    setNotification("error")
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                setNotification("server isn't responding")
            }
        })
        return succeed
    }


    fun setNotification(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }


    private fun shouldSend(newVal: Double, lastVal: Double): Boolean {
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