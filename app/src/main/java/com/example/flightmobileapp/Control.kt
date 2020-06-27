package com.example.flightmobileapp

import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.coroutines.*
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin


class Control : AppCompatActivity() {

    private var api: Api? = null
    private var controlManager = ControlManager(this)
    private var showImage: Boolean = true

    private lateinit  var image: ImageView
    private lateinit var rudderSeekbar: SeekBar
    private lateinit var throttleSeekbar: SeekBar
    private lateinit var rudder: TextView
    private lateinit var aileron: TextView
    private lateinit var elevator: TextView
    private lateinit var throttle: TextView
    private lateinit var joystick: JoystickView
    private lateinit var layout: RelativeLayout

    private var lastAileronVal = 0.0
    private var lastElevatorVal = 0.0
    private var lastThrottleVal = 0.0
    private var lastRudderVal = 0.0


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        val url = intent.getStringExtra("url")
        val connectionSucceed = controlManager.connect(url!!)
        if (connectionSucceed) {
            controlManager.connect(url)
            api = RetrofitBuilder.getApi()
            if (api == null) {
                // problem with server
                controlManager.setNotification("can't connect to server")
            }
            initView()
            initListeners()
        } else {
            controlManager.setNotification("connection failed")
        }
    }

    private fun initView() {
        image = findViewById(R.id.screenshot)
        rudderSeekbar = findViewById(R.id.rudder_seekbar);
        throttleSeekbar = findViewById(R.id.throttle_seekbar);
        rudder = findViewById(R.id.rudder)
        aileron = findViewById(R.id.aileron)
        elevator = findViewById(R.id.elevator)
        throttle = findViewById(R.id.throttle)
        joystick = findViewById(R.id.joystick)
        layout = findViewById(R.id.layout)
        joystick.bringToFront()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        initSeekBars()
        initJoystick()
    }


    private fun toRadians(angle: Int): Double {
        return angle.toDouble() * Math.PI / 180
    }

    private fun getNorm(strength: Int): Double {
        return strength.toDouble() / 100
    }

    private fun calcX(norm: Double, angle: Int): Double {
        val radians = toRadians(angle)
        return norm * cos(radians)
    }

    private fun calcY(norm: Double, angle: Int): Double {
        val radians = toRadians(angle)
        return -norm * sin(radians)

    }

    private fun initJoystick() {

        fun Double.round(decimals: Int): Double {
            var multiplier = 1.0
            repeat(decimals) { multiplier *= 10 }
            return round(this * multiplier) / multiplier
        }

        joystick.setOnMoveListener(object : JoystickView.OnMoveListener {
            override fun onMove(angle: Int, strength: Int) {
                val norm = getNorm(strength)
                val x = calcX(norm, angle)
                val y = calcY(norm, angle)

                aileron.text = x.round(3).toString()
                elevator.text = y.round(3).toString()
            }
        })
    }


    private fun setJoystick() {
        var sendToSimulator = false

        joystick.setOnMoveListener {
                angle, strength ->
            val aileron = cos(Math.toRadians(angle.toDouble())) * strength / 100
            val elevator = sin(Math.toRadians(angle.toDouble())) * strength / 100
            // the change is measured from the last value that sent to the server, therefore
            if (controlManager.shouldSend(aileron, lastAileronVal)) {
                controlManager.setAileron(aileron)
                sendToSimulator = true
            }
            if (controlManager.shouldSend(elevator, lastElevatorVal)) {
                controlManager.setElevator(elevator)
                sendToSimulator = true
            }
            // if the change is more than 1% we need to send the new values to the simulator
            if (sendToSimulator) {
                if (controlManager.sendCommand()) {
                    lastAileronVal = aileron
                    lastElevatorVal = elevator
                } else {
                    controlManager.setNotification("error in sending to server")
                }
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun initSeekBars() {
        initRudderBar()
        initThrottleBar()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRudderBar() {

        fun Double.round(decimals: Int): Double {
            var multiplier = 1.0
            repeat(decimals) { multiplier *= 10 }
            return round(this * multiplier) / multiplier
        }

        val min = -10
        val max = 10
        rudderSeekbar.max = max
        rudderSeekbar.min = min

        rudderSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                var progress = Utils.normalize(
                    p1.toDouble(),
                    (-1).toDouble(),
                    1.toDouble(),
                    min.toDouble(),
                    max.toDouble()
                )
                rudder.text = progress.round(2).toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initThrottleBar() {
        val min = 0
        val max = 10
        throttleSeekbar.max = max
        throttleSeekbar.min = min

        throttleSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val progress = Utils.normalize(
                    p1.toDouble(),
                    (0).toDouble(),
                    1.toDouble(),
                    min.toDouble(),
                    max.toDouble()
                )
                throttle.text = progress.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun setSeekBars() {
        var sendToSimulator = false
        // rudder

        fun Double.round(decimals: Int): Double {
            var multiplier = 1.0
            repeat(decimals) { multiplier *= 10 }
            return round(this * multiplier) / multiplier
        }

        val min = -10
        val max = 10
        rudderSeekbar.max = max
        rudderSeekbar.min = min

        rudderSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                var progress = Utils.normalize(
                    p1.toDouble(),
                    (-1).toDouble(),
                    1.toDouble(),
                    min.toDouble(),
                    max.toDouble()
                )
                rudder.text = progress.round(2).toString()
                // the change is measured from the last value that sent to the server, therefore
                if (controlManager.shouldSend(progress, lastRudderVal)) {
                    controlManager.setRudder(progress)
                    sendToSimulator = true
                }
            }

            override fun onStartTrackingTouch(seekBarX: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBarX: SeekBar?) {
            }

        })

        // throttle
        val minT = 0
        val maxT = 10
        throttleSeekbar.max = maxT
        throttleSeekbar.min = minT

        throttleSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                val progress = Utils.normalize(
                    p1.toDouble(),
                    (0).toDouble(),
                    1.toDouble(),
                    min.toDouble(),
                    max.toDouble()
                )
                throttle.text = progress.toString()
                // the change is measured from the last value that sent to the server, therefore
                if (controlManager.shouldSend(progress, lastThrottleVal)) {
                    controlManager.setThrottle(progress)
                    sendToSimulator = true
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        // if the change is more than 1% we need to send the new values to the simulator
        if (sendToSimulator) {
            if (controlManager.sendCommand()) {
                lastRudderVal = controlManager.getRudder()
                lastThrottleVal = controlManager.getThrottle()
            } else {
                controlManager.setNotification("error in sending to server")
            }
        }

    }


    override fun onStart() {
        super.onStart()
        showImage = true
        loopImg()
    }


    override fun onPause() {
        super.onPause()
        showImage = false
    }


    private fun loopImg() {
        CoroutineScope(Dispatchers.IO).launch {
            while(showImage) {
                controlManager.getImage(image)
                delay(2000)
            }
        }
    }

}


