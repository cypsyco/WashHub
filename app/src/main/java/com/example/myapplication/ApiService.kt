package com.example.myapplication

import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("/register")
    fun registerUser(@Body userData: User): Call<ApiResponse>

    @POST("/login")
    fun loginUser(@Body userData: User): Call<ApiResponse>

    @GET("/washers")
    fun getWashers(): Call<List<Washer>>

    @POST("/washerstatus/{id}")
    fun updateWasherStatus(
        @Path("id") id: Int,
        @Body timeSet: TimeSet
    ): Call<WasherStatusResponse>

    @POST("/checkUserId")
    fun checkUserId(@Body request: UserIdCheckRequest): Call<UserIdCheckResponse>

    @GET("/userdetails/{userid}")
    fun getUserDetails(@Path("userid") userid: String): Call<User>
}

data class User(
    val userid: String,
    val pw: String,
    val username: String,
    val dormitory: String,
    val gender: String,
    val image: String
)
data class ApiResponse(val message: Boolean, val UID: Int = -1)

data class Washer(
    val id: Int,
    val washername: String,
    val washerstatus: String,
    val starttime: Long,
    val settime: Long
)

data class TimeSet(
    val starttime: Long,
    val settime: Long
)

data class WasherStatusResponse(val washerstatus: String)

data class UserIdCheckRequest(val userid: String)
data class UserIdCheckResponse(val isAvailable: Boolean)
