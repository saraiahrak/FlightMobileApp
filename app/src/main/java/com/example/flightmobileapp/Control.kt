package com.example.flightmobileapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlin.math.round
import kotlinx.android.synthetic.main.activity_control.*

class Control : AppCompatActivity() {

    lateinit var rudderSeekbar: SeekBar
    lateinit var throttleSeekbar: SeekBar
    lateinit var rudder: TextView
    lateinit var aileron: TextView
    lateinit var elevator: TextView
    lateinit var throttle: TextView
    lateinit var knob: Button
    lateinit var knobLimits: Button

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
        knob = findViewById(R.id.knob)
        knobLimits = findViewById(R.id.background)
        rudder.text = "0"
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        initSeekBars()
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

        var min = -10
        var max = 10
        rudderSeekbar.max = max
        rudderSeekbar.min = min

        rudderSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                var progress = Utils.normalize(
                    p1.toDouble(),
                    (-1).toDouble(),
                    1.toDouble(),
                    (-10).toDouble(),
                    10.toDouble()
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
        var min = 0
        var max = 10
        throttleSeekbar.max = max
        throttleSeekbar.min = min

        throttleSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                var progress = Utils.normalize(
                    p1.toDouble(),
                    (0).toDouble(),
                    1.toDouble(),
                    (0).toDouble(),
                    10.toDouble()
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