package com.example.api

import com.example.api.model.FoodDTO
import com.example.api.model.requests.*
import com.example.api.model.responses.GetFoodListResponse
import com.example.api.model.responses.LoginResponseDTO
import com.example.api.model.responses.ResponseDTO
import retrofit2.Response
import retrofit2.http.*

internal interface AuthApiRetrofit {

    @POST("login")
    suspend fun login(@Body request: LoginRequestDTO): Response<LoginResponseDTO>

    @POST("register")
    suspend fun register(@Body request: RegisterRequestDTO): Response<ResponseDTO>

    @PATCH("users/{username}")
    suspend fun updateProfile(
        @Header("Authorization") authorization: String,
        @Path("username") username: String,
        @Body request: UpdateProfileRequestDTO
    ): Response<ResponseDTO>

    @DELETE("users/{username}")
    suspend fun deleteUser(
        @Header("Authorization") authorization: String,
        @Path("username") username: String
    ): Response<ResponseDTO>

    @GET("food")
    suspend fun getFoodList(
        @Header("Authorization") authorization: String,
        @Query("username") username: String?,
        @Query("from") from: Long?,
        @Query("to") to: Long?
    ): Response<GetFoodListResponse>

    @POST("food")
    suspend fun addFood(
        @Header("Authorization") authorization: String,
        @Body request: AddFoodRequestDTO
    ): Response<FoodDTO>

    @PATCH("food/{id}")
    suspend fun updateFood(
        @Header("Authorization") authorization: String,
        @Path("id") id: String,
        @Body request: UpdateFoodRequestDTO
    ): Response<FoodDTO>
}