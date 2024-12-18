package com.example.app.models

data class Product(
    val id: Int? = null, // id será nulo al crear un producto
    val name: String,
    val description: String,
    val category: String,
    val image: String,
    val price: Double
)
