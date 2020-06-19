package com.example.flightmobileapp

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi

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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initRudderBar() {
        var min = -1
        var max = 1
        var step = 50
        rudderSeekbar.max = max
        rudderSeekbar.min = min
    }
}