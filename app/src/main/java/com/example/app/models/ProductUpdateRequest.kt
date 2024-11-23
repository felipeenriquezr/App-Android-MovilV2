package com.example.app.models

data class ProductUpdateRequest(
    val name: String,
    val description: String,
    val price: Double
)
