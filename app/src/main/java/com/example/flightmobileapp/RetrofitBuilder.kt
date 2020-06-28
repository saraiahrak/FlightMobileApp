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
            val json = GsonBuilder()
                .setLenient()
                .create()
            val retrofit = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:5402/")
                .addConverterFactory(GsonConverterFactory.create(json))
                .build()

            val api = retrofit.create(Api::class.java)
            INSTANCE = api
            return api
        }

        fun getApi(): Api {
            var tmp = INSTANCE
            if (tmp != null) {
                return tmp
            }

            synchronized(this) {
                val current = build("123")
                INSTANCE = current
                return current
            }
        }
    }
}