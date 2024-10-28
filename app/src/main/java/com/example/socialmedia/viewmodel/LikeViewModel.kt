package com.example.socialmedia.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LikeViewModel : ViewModel() {
    private val _likedPosts: MutableStateFlow<MutableSet<Int>> = MutableStateFlow(mutableSetOf())
    val likedPosts: StateFlow<Set<Int>> get() = _likedPosts

    fun addPost(id: Int) {
        _likedPosts.value.add(id)
    }

    fun removePost(id: Int) {
        _likedPosts.value.remove(id)
    }
}