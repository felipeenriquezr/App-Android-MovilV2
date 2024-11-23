package com.example.app.models

data class AuthResponse(
    val userId: String,
    val username: String,
    val email: String,
    val token: String
)
