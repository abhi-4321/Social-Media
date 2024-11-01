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
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthenticatedClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UnauthenticatedClient

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    @UnauthenticatedClient
    fun providesLoginApi(@UnauthenticatedClient builder: Retrofit.Builder): LoginApi {
        return builder.build().create(LoginApi::class.java)
    }

    @Provides
    @Singleton
    @AuthenticatedClient
    fun providesPostApi(@AuthenticatedClient builder: Retrofit.Builder): PostApi {
        return builder.build().create(PostApi::class.java)
    }

    @Provides
    @Singleton
    @UnauthenticatedClient
    fun providesBaseOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().build()
    }

    @Provides
    @Singleton
    @AuthenticatedClient
    fun providesAuthOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(authInterceptor).build()
    }

    @Provides
    @Singleton
    @UnauthenticatedClient
    fun providesBaseRetrofit(@UnauthenticatedClient okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Provides
    @Singleton
    @AuthenticatedClient
    fun providesAuthRetrofit(@AuthenticatedClient okHttpClient: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
    }
}