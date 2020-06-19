package com.example.flightmobileapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView

class Control : AppCompatActivity() {

    lateinit var rudderSeekbar: SeekBar
    lateinit var rudder: TextView
    lateinit var aileron: TextView
    lateinit var elevator: TextView
    lateinit var knob: Button
    lateinit var knobLimits: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_control)
        initView()
        initListeners()
    }

    private fun initView() {
        rudderSeekbar = findViewById(R.id.rudder_seekbar);
        rudder = findViewById(R.id.rudder)
        aileron = findViewById(R.id.aileron)
        elevator = findViewById(R.id.elevator)
        knob = findViewById(R.id.knob)
        knobLimits = findViewById(R.id.background)
    }

    private fun initListeners() {
        initSeekBars()
    }

    private fun initSeekBars() {
        initRudderBar()
    }

    private fun initRudderBar() {
        rudderSeekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                TODO("Not yet implemented")
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                TODO("Not yet implemented")
            }

        })

    }
}