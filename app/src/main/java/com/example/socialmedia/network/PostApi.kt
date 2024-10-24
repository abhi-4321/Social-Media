package com.example.socialmedia.network

import androidx.room.Delete
import com.example.socialmedia.model.Comment
import com.example.socialmedia.model.CreatePost
import com.example.socialmedia.model.Post
import com.example.socialmedia.model.PostComment
import com.example.socialmedia.model.Profile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApi {

    @GET("/home/createpost/")
    suspend fun getAllPosts() : Response<List<Post>>

    @POST("/home/createpost/")
    suspend fun createPost(@Body post: CreatePost) : Response<Post>

    @GET("/home/createpost/{id}/")
    suspend fun getPostById(@Path("id") id: Int): Response<Post>

    @GET("/home/currpost/")
    suspend fun getAllPostsByUser() : Response<List<Post>>

    @POST("home/comment/{id}/")
    suspend fun postComment(@Path("id") postId: Int, @Body postComment: PostComment) : Response<Comment>

    @GET("home/profile/")
    suspend fun getProfile() : Response<Profile>
}