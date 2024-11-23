package com.example.app.models

data class ProductCreateRequest(
    val name: String,
    val description: String,
    val image: String,
    val price: Double
)
