package com.example.socialmedia.util

sealed class PostResult<T>(val data:T? = null) {
    class Success<T>(data: T?) : PostResult<T>(data)
    class Failure<T> : PostResult<T>()
    class Loading<T> : PostResult<T>()
}