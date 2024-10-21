package com.example.socialmedia.network

import android.content.Context
import com.example.socialmedia.util.Constants
import com.example.socialmedia.util.TokenManager
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitService(private val tokenManager: TokenManager) {

    companion object {
        @Volatile
        private var instance: LoginApi? = null

        @Synchronized
        fun getInstance(): LoginApi {
            if (instance == null)
                instance = Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.BASE_URL)
                    .build()
                    .create(LoginApi::class.java)
            return instance as LoginApi
        }
    }
}