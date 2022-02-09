package com.example.api

import com.example.api.model.ApiResult
import com.example.api.model.requests.LoginRequestDTO
import com.example.api.model.requests.RegisterRequestDTO
import com.example.api.model.requests.UpdateProfileRequestDTO
import com.example.api.model.responses.GetFoodListResponse
import com.example.api.model.responses.LoginResponseDTO
import com.example.api.model.responses.ResponseDTO
import retrofit2.Response
import retrofit2.http.Header
import retrofit2.http.Query

interface CalorieGuideApi {

    suspend fun login(request: LoginRequestDTO): ApiResult<LoginResponseDTO>

    suspend fun register(request: RegisterRequestDTO): ApiResult<ResponseDTO>

    suspend fun updateProfile(
        authorization: String,
        username: String,
        request: UpdateProfileRequestDTO
    ): ApiResult<ResponseDTO>

    suspend fun deleteUser(
        authorization: String,
        username: String
    ): ApiResult<ResponseDTO>

    suspend fun getFoodList(
        @Header("Authorization") authorization: String,
        @Query("username") username: String?,
        @Query("from") from: Long?,
        @Query("to") to: Long?
    ): ApiResult<GetFoodListResponse>
}