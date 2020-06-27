package com.example.flightmobileapp

import kotlin.math.round

class Utils {

    companion object {

        fun normalize(
            n: Double,
            a: Double,
            b: Double,
            xMin: Double,
            xMax: Double
        ): Double {
            return (b - a) * ((n - xMin) / (xMax - xMin)) + a
        }
    }

}