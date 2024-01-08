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

    @GET("/dryers")
    fun getDryers(): Call<List<Dryer>>

    @GET("/washers1")
    fun getWashersDorm1(): Call<List<Washer>>

    @GET("/washers2")
    fun getWashersDorm2(): Call<List<Washer>>

    @GET("/washers3")
    fun getWashersDorm3(): Call<List<Washer>>

    @GET("/washers4")
    fun getWashersDorm4(): Call<List<Washer>>

    @POST("/washerstatus/{id}")
    fun updateWasherStatus(
        @Path("id") id: Int,
        @Body timeSet: TimeSet
    ): Call<WasherStatusResponse>

    @POST("/checkUserId")
    fun checkUserId(@Body request: UserIdCheckRequest): Call<UserIdCheckResponse>

    @GET("/userdetails/{userid}")
    fun getUserDetails(@Path("userid") userid: String): Call<User>

    @POST("/washerend/{id}")
    fun endWasherSession(
        @Path("id") id: Int
    ): Call<ApiResponse>

    @GET("/washerReservations/{washerId}")
    fun getWasherReservations(@Path("washerId") washerId: Int): Call<List<String>>

    @GET("/userReservations/{userId}")
    fun getUserReservations(@Path("userId") userId: String): Call<List<ReservationResponse>>

    @POST("/reserveWasher")
    fun reserveWasher(@Body reservationRequest: ReservationRequest): Call<ApiResponse>

    @GET("/washersByUser/{userid}")
    fun getWashersByUser(@Path("userid") userid: String): Call<List<Washer>>

    @GET("/washersByUser/{userid}")
    fun getDryersByUser(@Path("userid") userid: String): Call<List<Dryer>>

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
    var washerstatus: String,
    val starttime: Long,
    val settime: Long
)

data class Dryer(
    val id: Int,
    val dryername: String,
    var dryerstatus: String,
    val starttime: Long,
    val settime: Long
)

data class TimeSet(
    val starttime: Long,
    val settime: Long,
    val userid: String
)

data class WasherStatusResponse(val washerstatus: String)

data class UserIdCheckRequest(val userid: String)
data class UserIdCheckResponse(val isAvailable: Boolean)

data class ReservationResponse(
    val washername: String?,
    val dryername: String?
)

data class ReservationRequest(val userid: String, val washerId: Int)

