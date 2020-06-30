package com.example.flightmobileapp;

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitBuilder {
    companion object {
        private var INSTANCE: Api? = null
        private var URL: String? = null

        fun build(url: String): Api {
            URL = url
            val json = GsonBuilder()
                .setLenient()
                .create()
            val retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(json))
                .build()

            val api = retrofit.create(Api::class.java)
            INSTANCE = api
            return api
        }

        fun getApi(url: String): Api {
            var tmp = INSTANCE
            if (tmp != null && URL == url) {
                return tmp
            }

            synchronized(this) {
                val current = build(url)
                INSTANCE = current
                return current
            }
        }
    }
}