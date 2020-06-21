package com.example.flightmobileapp

import android.content.ClipData
import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import kotlin.math.round


class Control : AppCompatActivity() {

    private lateinit var rudderSeekbar: SeekBar
    private lateinit var throttleSeekbar: SeekBar
    private lateinit var rudder: TextView
    private lateinit var aileron: TextView
    private lateinit var elevator: TextView
    private lateinit var throttle: TextView
    private lateinit var knob: Button
    private lateinit var knobLimits: Button

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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        initSeekBars()
        initKnob()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initKnob() {
        knob.isLongClickable = true

        knob.setOnLongClickListener(object : View.OnLongClickListener {
            override fun onLongClick(p0: View?): Boolean {
                return true
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