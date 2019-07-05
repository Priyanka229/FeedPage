package com.example.feedpage.network

import android.content.Context
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import com.google.gson.GsonBuilder
import retrofit2.converter.scalars.ScalarsConverterFactory


class RetroFitClient {

    companion object {
        private val LOCK = Any()
        private val BASE_URL = "http://www.mocky.io/v2/"
        private var sInstance: RetroFitApiServices? = null

        fun getRetroFitService(context: Context): RetroFitApiServices {
            if (sInstance == null) {
                synchronized(LOCK) {
                    sInstance = getClient(context).create<RetroFitApiServices>(RetroFitApiServices::class.java)
                }
            }
            return sInstance!!
        }

        private fun getClient(mContext: Context): Retrofit {
            val interceptor = Interceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(interceptor)
            val client = httpClient.build()

            val gson = GsonBuilder()
                .setLenient()
                .create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
    }


}