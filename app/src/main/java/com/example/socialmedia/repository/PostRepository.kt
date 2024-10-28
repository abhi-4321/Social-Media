package com.example.socialmedia.repository

import com.example.socialmedia.db.AppDao
import com.example.socialmedia.db.entities.PostEntity
import com.example.socialmedia.di.AuthenticatedClient
import com.example.socialmedia.model.Comment
import com.example.socialmedia.model.CreatePost
import com.example.socialmedia.model.Post
import com.example.socialmedia.model.PostComment
import com.example.socialmedia.model.Profile
import com.example.socialmedia.network.PostApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

class PostRepository @Inject constructor(private val appDao: AppDao) {

    @Inject
    @AuthenticatedClient
    lateinit var postApi: PostApi

    suspend fun getAllPosts() : Response<List<Post>> {
        return postApi.getAllPosts()
    }

    suspend fun createPost(post: CreatePost) : Response<Post> {
        return postApi.createPost(post)
    }

    suspend fun getProfile() : Response<Profile> {
        return postApi.getProfile()
    }

    suspend fun getPostById(id: Int): Response<Post> {
        return postApi.getPostById(id)
    }

    suspend fun getAllPostsByUser() : Response<List<Post>> {
        return postApi.getAllPostsByUser()
    }

    suspend fun deletePost(id: Int) : Response<Void> {
        return postApi.deletePost(id)
    }

    suspend fun likePost(id: Int) : Response<Void> {
        return postApi.likePost(id)
    }

    fun getAllPostIds() : Flow<List<Int>> {
        return appDao.getAllPostIds()
    }

    suspend fun savePostId(postId: Int) {
        appDao.upsert(PostEntity(postId))
    }

    suspend fun deletePostId(postId: Int) {
        appDao.delete(postId)
    }

    suspend fun postComment(postId: Int, comm: String) : Response<Comment> {
        return postApi.postComment(postId, PostComment(comm))
    }

}