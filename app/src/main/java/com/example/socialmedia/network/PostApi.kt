package com.example.socialmedia.network

import com.example.socialmedia.model.Comment
import com.example.socialmedia.model.CreatePost
import com.example.socialmedia.model.Post
import com.example.socialmedia.model.PostComment
import com.example.socialmedia.model.Profile
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostApi {

    @GET("post/")
    suspend fun getAllPosts() : Response<List<Post>>

    @POST("post/")
    suspend fun createPost(@Body post: CreatePost) : Response<Post>

    @GET("post/{id}/")
    suspend fun getPostById(@Path("id") id: Int): Response<Post>

    @GET("userPost/")
    suspend fun getAllPostsByUser() : Response<List<Post>>

    @POST("comment/{id}/")
    suspend fun postComment(@Path("id") postId: Int, @Body postComment: PostComment) : Response<Comment>

    @GET("profile/")
    suspend fun getProfile() : Response<Profile>

    @DELETE("post/{id}/")
    suspend fun deletePost(@Path("id") id: Int) : Response<Void>

    @DELETE("post/{postId}/comment/{commentId}")
    suspend fun deleteComment(@Path("postId") postId: Int, @Path("commentId") commentId: Int) : Response<Void>

    @POST("post/{postId}/like/")
    suspend fun likePost(@Path("postId") id: Int) : Response<Void>
}