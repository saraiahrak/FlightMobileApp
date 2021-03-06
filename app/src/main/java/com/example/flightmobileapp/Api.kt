package com.example.flightmobileapp

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * Interface Api
 * HTTP Requests from server
 * */

interface Api {
    @GET("/screenshot")
    fun getScreenshot(): Call<ResponseBody>

    @POST("/api/Command")
    fun post(@Body rb: RequestBody): Call<ResponseBody>

}