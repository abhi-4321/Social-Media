package com.example.socialmedia.repository

import com.example.socialmedia.db.AppDao
import com.example.socialmedia.model.Post
import com.example.socialmedia.network.PostApi
import retrofit2.Response
import javax.inject.Inject

class PostRepository @Inject constructor(private val postApi: PostApi, private val appDao: AppDao) {

    suspend fun getAllPosts() : Response<List<Post>> {
        return postApi.getAllPosts()
    }

    suspend fun createPost(post: Post) : Response<Post> {
        return postApi.createPost(post)
    }

    suspend fun getPostById(id: Int): Response<Post> {
        return postApi.getPostById(id)
    }

}