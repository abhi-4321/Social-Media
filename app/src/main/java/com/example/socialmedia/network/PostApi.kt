package com.example.socialmedia.network

import com.example.socialmedia.model.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface PostApi {

    @GET("/home/createpost/")
    suspend fun getAllPosts() : Response<List<Post>>

    @POST("/home/createpost/")
    suspend fun createPost(post: Post) : Response<Post>
}