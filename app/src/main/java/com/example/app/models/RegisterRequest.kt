package com.example.app.models

import java.sql.Date

data class RegisterRequest(
    val firstName: String,
    val lastName: String,
    val username: String,
    val email: String,
    val password: String,
    val brithOfDate: String,
    val phoneNumber: String,
    val role: String
)
