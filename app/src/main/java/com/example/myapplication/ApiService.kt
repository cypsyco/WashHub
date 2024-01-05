package com.example.myapplication

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("/register")
    fun registerUser(@Body userData: User): Call<ApiResponse>

    @POST("/login")
    fun loginUser(@Body userData: User): Call<ApiResponse>

    @GET("/washers")
    fun getWashers(): Call<List<Washer>>
}

data class User(val username: String, val pw: String)
data class ApiResponse(val message: Boolean, val UID: Int = -1)
