package com.example.socialmedia.model

data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String,
    val password2: String,
    val tc: String = "true"
)
