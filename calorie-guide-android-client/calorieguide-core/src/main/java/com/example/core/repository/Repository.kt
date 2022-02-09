package com.example.core.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.example.core.model.Food
import com.example.core.model.Profile
import com.example.core.model.RepositoryResult
import com.example.core.model.requests.LoginRequest
import com.example.core.model.requests.RegisterRequest
import com.example.core.model.requests.UpdateProfileRequest
import com.example.core.model.responses.LoginResponse
import com.example.core.model.responses.Response

interface Repository {

    suspend fun login(request: LoginRequest): RepositoryResult<LoginResponse>

    suspend fun register(request: RegisterRequest): RepositoryResult<Response>

    fun signOut()

    fun onSignOut() : LiveData<Boolean>

    fun getToken(): String?

    fun getProfile(): Profile?

    suspend fun updateProfile(request: UpdateProfileRequest): RepositoryResult<Response>

    suspend fun deleteUser(): RepositoryResult<Response>

    suspend fun syncMyFoodEntries(from: Long, to: Long): RepositoryResult<Boolean>

    suspend fun syncFoodEntries(username: String, from: Long, to: Long): RepositoryResult<Boolean>

    fun getMyFoodEntries(from: Long, to: Long): LiveData<PagedList<Food>>

    fun getFoodEntries(username: String, from: Long, to: Long): LiveData<PagedList<Food>>
}