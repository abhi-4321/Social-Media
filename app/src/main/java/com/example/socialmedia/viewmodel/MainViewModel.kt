package com.example.socialmedia.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialmedia.model.Post
import com.example.socialmedia.repository.PostRepository
import com.example.socialmedia.util.PostResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val postRepository: PostRepository) : ViewModel() {

    private val _allPostsFlow: MutableStateFlow<List<Post>> = MutableStateFlow(emptyList())
    val allPostsFlow: StateFlow<List<Post>> get() = _allPostsFlow


    fun getAllPosts() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = postRepository.getAllPosts()
            if (response.isSuccessful && response.body() != null) {
                _allPostsFlow.emit(response.body()!!)
            } else {
                _allPostsFlow.emit(emptyList())
            }
        }
    }

    val createPostCallback = MutableStateFlow(false)
    fun createPost(post: Post) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = postRepository.createPost(post)
            if (response.isSuccessful && response.code() == 201) {
                createPostCallback.emit(true)
            } else {
                createPostCallback.emit(false)
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
}

