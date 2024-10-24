package com.example.socialmedia.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.model.Comment
import com.example.socialmedia.model.CreatePost
import com.example.socialmedia.model.Post
import com.example.socialmedia.model.Profile
import com.example.socialmedia.repository.PostRepository
import com.example.socialmedia.util.PostResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {

    private val _allPostsFlow: MutableStateFlow<List<Post>> = MutableStateFlow(emptyList())
    val allPostsFlow: StateFlow<List<Post>> get() = _allPostsFlow


    fun getAllPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = postRepository.getAllPosts()
            Log.d("HomeFrag", "$response ${response.code()}")
            if (response.isSuccessful && response.body() != null) {
                _allPostsFlow.emit(response.body()!!)
            } else {
                _allPostsFlow.emit(emptyList())
            }
        }
    }

    val createPostCallback = MutableStateFlow<PostResult<Post>>(PostResult.Loading())
    fun createPost(post: CreatePost) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = postRepository.createPost(post)
            if (response.isSuccessful && response.body() != null) {
                createPostCallback.emit(PostResult.Success(response.body()!!))
            } else {
                createPostCallback.emit(PostResult.Failure())
            }
        }
    }

    private val _postFlow = MutableStateFlow<PostResult<Post>>(PostResult.Loading())
    val postFlow: StateFlow<PostResult<Post>> get() = _postFlow

    fun getPostById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = postRepository.getPostById(id)
            if (response.isSuccessful && response.body() != null) {
                _postFlow.emit(PostResult.Success(response.body()!!))
            } else {
                _postFlow.emit(PostResult.Failure())
            }
        }
    }

    private val _profileFlow = MutableStateFlow<PostResult<Profile>>(PostResult.Loading())
    val profileFlow: StateFlow<PostResult<Profile>> get() = _profileFlow

    fun getProfile()  {
        viewModelScope.launch(Dispatchers.IO) {
            val response = postRepository.getProfile()
            if (response.isSuccessful && response.body() != null) {
                _profileFlow.emit(PostResult.Success(response.body()!!))
            } else {
                _profileFlow.emit(PostResult.Failure())
            }
        }
    }

    private val _allPostsByUserFlow = MutableStateFlow<PostResult<List<Post>>>(PostResult.Loading())
    val allPostsByUserFlow: StateFlow<PostResult<List<Post>>> get() = _allPostsByUserFlow

    fun getAllPostsByUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = postRepository.getAllPostsByUser()
            if (response.isSuccessful && response.body() != null) {
                _allPostsByUserFlow.emit(PostResult.Success(response.body()!!))
            } else {
                _allPostsByUserFlow.emit(PostResult.Failure())
            }
        }
    }

    val commentCallback = MutableStateFlow<PostResult<Comment>>(PostResult.Loading())

    fun postComment(postId: Int, comm: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = postRepository.postComment(postId,comm)
            if (response.isSuccessful && response.body() != null) {
                commentCallback.emit(PostResult.Success(response.body()!!))
            } else {
                commentCallback.emit(PostResult.Failure())
            }
        }
    }

    fun getAllPostIds() : Flow<List<Int>> {
        return postRepository.getAllPostIds()
    }

    fun upsert(postId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.upsert(postId)
        }
    }

    suspend fun delete(postId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            postRepository.delete(postId)
        }
    }

}

