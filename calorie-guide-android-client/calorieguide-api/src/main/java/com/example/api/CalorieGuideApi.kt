package com.example.api

import com.example.api.model.ApiResult
import com.example.api.model.FoodDTO
import com.example.api.model.requests.*
import com.example.api.model.responses.GetFoodListResponse
import com.example.api.model.responses.LoginResponseDTO
import com.example.api.model.responses.ResponseDTO

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
        authorization: String,
        username: String?,
        from: Long?,
        to: Long?
    ): ApiResult<GetFoodListResponse>

    suspend fun addFood(
        authorization: String,
        request: AddFoodRequestDTO
    ): ApiResult<FoodDTO>


    suspend fun updateFood(
        authorization: String,
        id: String,
        request: UpdateFoodRequestDTO
    ): ApiResult<FoodDTO>
}