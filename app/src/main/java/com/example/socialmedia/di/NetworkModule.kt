package com.example.socialmedia.di

import com.example.socialmedia.network.AuthInterceptor
import com.example.socialmedia.network.LoginApi
import com.example.socialmedia.network.PostApi
import com.example.socialmedia.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesLoginApi(builder: Retrofit.Builder): LoginApi {
        return builder.build().create(LoginApi::class.java)
    }

    @Provides
    @Singleton
    fun providesPostApi(builder: Retrofit.Builder): PostApi {
        return builder.build().create(PostApi::class.java)
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
    }
}