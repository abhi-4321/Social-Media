package com.example.socialmedia.network

import com.example.socialmedia.model.LoginRequest
import com.example.socialmedia.model.LoginResponse
import com.example.socialmedia.model.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface LoginApi {

    @POST("login/")
    suspend fun login(@Body loginRequest: LoginRequest) : Response<LoginResponse>

    @POST("register/")
    suspend fun register(@Body signUpRequest: SignUpRequest) : Response<LoginResponse>

}