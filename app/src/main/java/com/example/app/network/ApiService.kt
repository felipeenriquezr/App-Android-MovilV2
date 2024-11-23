package com.example.app.network

import com.example.app.models.AuthResponse
import com.example.app.models.LoginRequest
import com.example.app.models.Product
import com.example.app.models.ProductCreateRequest
import com.example.app.models.ProductUpdateRequest
import com.example.app.models.RegisterRequest
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    // Auth endpoints
    @POST("Auth/Login")
    fun login(@Body loginRequest: LoginRequest): Call<AuthResponse>

    @POST("Auth/Register")
    suspend fun register(@Body request: RegisterRequest): Response<AuthResponse>


    // Product endpoints
    @GET("Product")
    fun getAllProducts(): Call<List<Product>>

    @GET("Product/{id}")
    suspend fun getProductById(@Path("id") id: Int): Response<Product>

    @POST("Product")
    suspend fun createProduct(@Body product: ProductCreateRequest): Response<Void>

    @PUT("Product/{id}")
    suspend fun updateProduct(
        @Path("id") id: Int, // Cambiar a Int
        @Body productUpdateRequest: ProductUpdateRequest
    ): Response<Void>

    @DELETE("Product/{id}")
    fun deleteProduct(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Call<Void>
}