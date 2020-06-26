package com.example.flightmobileapp

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin


class Control : AppCompatActivity() {

    private lateinit var rudderSeekbar: SeekBar
    private lateinit var throttleSeekbar: SeekBar
    private lateinit var rudder: TextView
    private lateinit var aileron: TextView
    private lateinit var elevator: TextView
    private lateinit var throttle: TextView
    private lateinit var joystick: JoystickView
    private lateinit var layout: RelativeLayout

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        initView()
        initListeners()
    }

    private fun initView() {
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
}

