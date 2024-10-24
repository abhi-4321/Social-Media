package com.example.socialmedia.network

import android.util.Log
import com.example.socialmedia.util.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val tokenManager: TokenManager) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        val token = tokenManager.getToken() ?: "  "
        Log.d("AccessToken",token + " iF Null")
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}