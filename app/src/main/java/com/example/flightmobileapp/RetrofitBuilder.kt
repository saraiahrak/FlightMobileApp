package com.example.flightmobileapp;

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class RetrofitBuilder {
    companion object {
        private var INSTANCE: Api? = null
        private var URL: String? = null

        fun build(url: String): Api {
            val client:OkHttpClient  = OkHttpClient.Builder()
                .connectTimeout(10,TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .build()

            val tempRetVal = INSTANCE
            if (tempRetVal != null && url == URL) {
                return tempRetVal
            }
            val json = GsonBuilder()
                .setLenient()
                .create()
            val instance = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(json))
                //.addCallAdapterFactory(CoroutineCallAdapterFactory())
                .client(client)
                .build().create(Api::class.java)
            INSTANCE = instance
            return instance
        }

        fun getApi(): Api? {
            return INSTANCE
        }
    }
}