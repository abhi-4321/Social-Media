package com.example.socialmedia.model

data class Post(
    val id: Int,
    val text: String,
    val created_by: String,
    val comments: List<Comment>
)
