package com.example.socialmedia.repository

import com.example.socialmedia.model.Post
import com.example.socialmedia.network.PostApi
import retrofit2.Response

class PostRepository(private val postApi: PostApi) {

    suspend fun getAllPosts() : Response<List<Post>> {
        return postApi.getAllPosts()
    }

    suspend fun createPost(post: Post) : Response<Post> {
        return postApi.createPost(post)
    }

}