package com.example.flightmobileapp

import com.google.gson.annotations.SerializedName

/*data class Command(
    @SerializedName("rudder") val rudder: Float?,
    @SerializedName("elevator") val elevator: Float?,
    @SerializedName("aileron") val aileron: Float?,
    @SerializedName("throttle") val throttle: Float?
)*/

data class Command (
    var Aileron: Float = 0f,
    var Rudder: Float =0f ,
    var Elevator: Float =0f ,
    var Throttle: Float =0f
)
