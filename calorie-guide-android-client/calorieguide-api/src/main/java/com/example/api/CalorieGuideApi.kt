package com.example.api

import com.example.api.model.ApiResult
import com.example.api.model.FoodDTO
import com.example.api.model.requests.*
import com.example.api.model.responses.*

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
    ): ApiResult<GetFoodListResponseDTO>

    suspend fun addFood(
        authorization: String,
        request: AddFoodRequestDTO
    ): ApiResult<FoodDTO>

    suspend fun updateFood(
        authorization: String,
        id: String,
        request: UpdateFoodRequestDTO
    ): ApiResult<FoodDTO>

    suspend fun deleteFood(authorization: String, id: String): ApiResult<ResponseDTO>

    suspend fun getUsers(
        authorization: String,
        exclusiveStartKey: String?
    ): ApiResult<GetUsersResponseDTO>

    suspend fun getFoodEntryCount(
        authorization: String,
        from: Long?,
        to: Long?
    ): ApiResult<GetFoodEntryCountResponseDTO>

    suspend fun getUsersAverageCalories(
        authorization: String,
        from: Long?,
        to: Long?
    ): ApiResult<GetUsersAverageCaloriesDTO>
}