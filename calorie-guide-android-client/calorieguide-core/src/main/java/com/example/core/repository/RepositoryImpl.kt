package com.example.core.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.example.core.storage.DataProvider
import com.example.api.CalorieGuideApi
import com.example.api.model.ApiResult
import com.example.core.model.*
import com.example.core.model.converters.reqests.toDTO
import com.example.core.model.converters.responses.toEntityList
import com.example.core.model.converters.responses.toModel
import com.example.core.model.converters.toModel
import com.example.core.model.requests.*
import com.example.core.model.responses.LoginResponse
import com.example.core.model.responses.Response
import com.example.core.storage.room.AppDatabase
import com.example.core.storage.room.toEntry
import com.example.core.storage.room.toModel
import com.example.core.utils.PagedListConverter
import com.example.core.utils.SingleLiveEvent

internal class RepositoryImpl(
    private val calorieGuideApi: CalorieGuideApi,
    private val dataProvider: DataProvider,
    private val db: AppDatabase
) : Repository {

    companion object {
        const val TOKEN_KEY = "token"
        const val PROFILE_KEY = "profile"
    }

    private val _onSignOut = SingleLiveEvent<Boolean>()
    private val onSignOut: LiveData<Boolean> = _onSignOut

    override suspend fun login(request: LoginRequest): RepositoryResult<LoginResponse> =
        when (val result = calorieGuideApi.login(request.toDTO())) {
            is ApiResult.Success -> {
                val loginResult = result.data.toModel()
                dataProvider.writeValue(TOKEN_KEY, "Bearer ${loginResult.token}")
                val profile = Profile(
                    loginResult.username,
                    loginResult.role,
                    loginResult.dailyCalorieLimit,
                    loginResult.firstName,
                    loginResult.lastName,
                    loginResult.gender,
                    loginResult.birthday,
                )
                dataProvider.writeValue(PROFILE_KEY, profile)
                RepositoryResult.Success(loginResult)
            }
            is ApiResult.Error -> RepositoryResult.Error(result.code, result.message)
            is ApiResult.NetworkError -> RepositoryResult.NetworkError(result.message)
            is ApiResult.UnknownError -> RepositoryResult.UnknownError(result.message)
            is ApiResult.SessionExpired -> RepositoryResult.Error(ErrorCode.UNAUTHORIZED.code, "")
        }

    override suspend fun register(request: RegisterRequest): RepositoryResult<Response> =
        when (val result = calorieGuideApi.register(request.toDTO())) {
            is ApiResult.Success -> RepositoryResult.Success(result.data.toModel())
            is ApiResult.Error -> RepositoryResult.Error(result.code, result.message)
            is ApiResult.NetworkError -> RepositoryResult.NetworkError(result.message)
            is ApiResult.UnknownError -> RepositoryResult.UnknownError(result.message)
            is ApiResult.SessionExpired -> RepositoryResult.Error(ErrorCode.UNAUTHORIZED.code, "")
        }

    override suspend fun signOut() {
        if (getToken() != null) {
            dataProvider.remove(TOKEN_KEY)
            dataProvider.remove(PROFILE_KEY)
            db.foodDao().deleteAll()
            db.userDao().deleteAll()
            _onSignOut.value = true
        }
    }

    override fun onSignOut(): LiveData<Boolean> = onSignOut

    override fun getToken() = dataProvider.readValue<String>(TOKEN_KEY, String::class.java)

    override fun getProfile(): Profile? = dataProvider.readValue(PROFILE_KEY, Profile::class.java)

    override suspend fun syncProfile(): RepositoryResult<Boolean> =
        when (val result = calorieGuideApi.getUser(getToken() ?: "",
            getProfile()?.username ?: "")) {
            is ApiResult.Success -> {
                val user = result.data.toModel()
                if (user != null) {
                    val profile = Profile(
                        user.username,
                        user.role,
                        user.dailyCalorieLimit,
                        user.firstName,
                        user.lastName,
                        user.gender,
                        user.birthday,
                    )
                    dataProvider.writeValue(PROFILE_KEY, profile)
                    RepositoryResult.Success(true)
                } else RepositoryResult.UnknownError("")
            }
            is ApiResult.Error -> RepositoryResult.Error(result.code, result.message)
            is ApiResult.NetworkError -> RepositoryResult.NetworkError(result.message)
            is ApiResult.UnknownError -> RepositoryResult.UnknownError(result.message)
            is ApiResult.SessionExpired -> {
                signOut()
                RepositoryResult.Error(ErrorCode.UNAUTHORIZED.code, "")
            }
        }

    override suspend fun updateProfile(request: UpdateProfileRequest): RepositoryResult<Response> =
        when (val result = calorieGuideApi.updateProfile(getToken() ?: "",
            request.profile.username, request.toDTO())) {
            is ApiResult.Success -> {
                dataProvider.writeValue(PROFILE_KEY, request.profile)
                RepositoryResult.Success(result.data.toModel())
            }
            is ApiResult.Error -> RepositoryResult.Error(result.code, result.message)
            is ApiResult.NetworkError -> RepositoryResult.NetworkError(result.message)
            is ApiResult.UnknownError -> RepositoryResult.UnknownError(result.message)
            is ApiResult.SessionExpired -> {
                signOut()
                RepositoryResult.Error(ErrorCode.UNAUTHORIZED.code, "")
            }
        }

    override suspend fun deleteUser(): RepositoryResult<Response> =
        when (val result = calorieGuideApi.deleteUser(getToken() ?: "",
            getProfile()?.username ?: "")) {
            is ApiResult.Success -> {
                signOut()
                RepositoryResult.Success(result.data.toModel())
            }
            is ApiResult.Error -> RepositoryResult.Error(result.code, result.message)
            is ApiResult.NetworkError -> RepositoryResult.NetworkError(result.message)
            is ApiResult.UnknownError -> RepositoryResult.UnknownError(result.message)
            is ApiResult.SessionExpired -> {
                signOut()
                RepositoryResult.Error(ErrorCode.UNAUTHORIZED.code, "")
            }
        }

    override suspend fun syncMyFoodEntries(from: Long, to: Long) =
        syncFoodEntries(getProfile()?.username ?: "", from, to)

    override suspend fun syncFoodEntries(username: String, from: Long, to: Long)  =
        when (val result = calorieGuideApi.getFoodList(getToken() ?: "",
            username, from, to)) {
            is ApiResult.Success -> {
                db.foodDao().deleteFoodEntriesByTimeRange(username, from, to)
                db.foodDao().insertAll(result.data.toEntityList())
                RepositoryResult.Success(true)
            }
            is ApiResult.Error -> RepositoryResult.Error(result.code, result.message)
            is ApiResult.NetworkError -> RepositoryResult.NetworkError(result.message)
            is ApiResult.UnknownError -> RepositoryResult.UnknownError(result.message)
            is ApiResult.SessionExpired -> {
                signOut()
                RepositoryResult.Error(ErrorCode.UNAUTHORIZED.code, "")
            }
        }

    override fun getMyFoodEntries(from: Long, to: Long): LiveData<PagedList<Food>> =
        getFoodEntries(getProfile()?.username ?: "", from, to)

    override fun getFoodEntries(
        username: String,
        from: Long,
        to: Long
    ): LiveData<PagedList<Food>> = db.foodDao().getFoodEntriesByTimeRange(username, from, to)
        .map { it.toModel() }.toLiveData(20)

    override suspend fun addFood(request: AddFoodRequest): RepositoryResult<Food>  =
        when (val result = calorieGuideApi.addFood(getToken() ?: "", request.toDTO())) {
            is ApiResult.Success -> {
                val newFood = result.data.toModel()
                if (newFood != null) {
                    db.foodDao().insert(newFood.toEntry())
                    RepositoryResult.Success(newFood)
                } else {
                    RepositoryResult.UnknownError("")
                }
            }
            is ApiResult.Error -> RepositoryResult.Error(result.code, result.message)
            is ApiResult.NetworkError -> RepositoryResult.NetworkError(result.message)
            is ApiResult.UnknownError -> RepositoryResult.UnknownError(result.message)
            is ApiResult.SessionExpired -> {
                signOut()
                RepositoryResult.Error(ErrorCode.UNAUTHORIZED.code, "")
            }
        }

    override suspend fun updateFood(id: String, request: UpdateFoodRequest):
            RepositoryResult<Food>  =
        when (val result = calorieGuideApi.updateFood(getToken() ?: "", id,
            request.toDTO())) {
            is ApiResult.Success -> {
                val updatedFood = result.data.toModel()
                if (updatedFood != null) {
                    db.foodDao().updateFood(updatedFood.id, updatedFood.name, updatedFood.timestamp,
                        updatedFood.calories)
                    RepositoryResult.Success(updatedFood)
                } else {
                    RepositoryResult.UnknownError("")
                }
            }
            is ApiResult.Error -> RepositoryResult.Error(result.code, result.message)
            is ApiResult.NetworkError -> RepositoryResult.NetworkError(result.message)
            is ApiResult.UnknownError -> RepositoryResult.UnknownError(result.message)
            is ApiResult.SessionExpired -> {
                signOut()
                RepositoryResult.Error(ErrorCode.UNAUTHORIZED.code, "")
            }
        }

    override suspend fun deleteFood(id: String): RepositoryResult<Response>  =
        when (val result = calorieGuideApi.deleteFood(getToken() ?: "", id)) {
            is ApiResult.Success -> {
                db.foodDao().deleteFood(id)
                RepositoryResult.Success(result.data.toModel())
            }
            is ApiResult.Error -> RepositoryResult.Error(result.code, result.message)
            is ApiResult.NetworkError -> RepositoryResult.NetworkError(result.message)
            is ApiResult.UnknownError -> RepositoryResult.UnknownError(result.message)
            is ApiResult.SessionExpired -> {
                signOut()
                RepositoryResult.Error(ErrorCode.UNAUTHORIZED.code, "")
            }
        }

    override suspend fun getMyCalorieSumByTimeRange(from: Long, to: Long) =
        getCalorieSumByTimeRange(getProfile()?.username ?: "", from, to)

    override suspend fun getCalorieSumByTimeRange(username: String, from: Long, to: Long) =
        db.foodDao().getCalorieSumByTimeRange(username, from, to)

    override suspend fun syncUserEntries(exclusiveStartKey: String?): RepositoryResult<Boolean>  =
        when (val result = calorieGuideApi.getUsers(getToken() ?: "", exclusiveStartKey)) {
            is ApiResult.Success -> {
                db.userDao().deleteAll()
                db.userDao().insertAll(result.data.toEntityList())
                RepositoryResult.Success(true)
            }
            is ApiResult.Error -> RepositoryResult.Error(result.code, result.message)
            is ApiResult.NetworkError -> RepositoryResult.NetworkError(result.message)
            is ApiResult.UnknownError -> RepositoryResult.UnknownError(result.message)
            is ApiResult.SessionExpired -> {
                signOut()
                RepositoryResult.Error(ErrorCode.UNAUTHORIZED.code, "")
            }
        }

    override fun getUserEntries(): LiveData<PagedList<User>> =
        db.userDao().getAll().map { it.toModel() }.toLiveData(20)

    override suspend fun getFoodEntryCount(from: Long, to: Long): RepositoryResult<Int>  =
        when (val result = calorieGuideApi.getFoodEntryCount(getToken() ?: "", from, to)) {
            is ApiResult.Success -> {
                RepositoryResult.Success(result.data.count ?: 0)
            }
            is ApiResult.Error -> RepositoryResult.Error(result.code, result.message)
            is ApiResult.NetworkError -> RepositoryResult.NetworkError(result.message)
            is ApiResult.UnknownError -> RepositoryResult.UnknownError(result.message)
            is ApiResult.SessionExpired -> {
                signOut()
                RepositoryResult.Error(ErrorCode.UNAUTHORIZED.code, "")
            }
        }

    override suspend fun getUsersAverageCalories(
        from: Long,
        to: Long
    ): RepositoryResult<PagedList<UserAverageCalories>>  =
        when (val result = calorieGuideApi.getUsersAverageCalories(
            getToken() ?: "", from, to)
        ) {
            is ApiResult.Success -> {
                val list: List<UserAverageCalories> =
                    result.data.items?.mapNotNull { it.toModel() } ?: listOf()
                val pagedList = PagedListConverter<UserAverageCalories>().convert(list, 20)
                RepositoryResult.Success(pagedList)
            }
            is ApiResult.Error -> RepositoryResult.Error(result.code, result.message)
            is ApiResult.NetworkError -> RepositoryResult.NetworkError(result.message)
            is ApiResult.UnknownError -> RepositoryResult.UnknownError(result.message)
            is ApiResult.SessionExpired -> {
                signOut()
                RepositoryResult.Error(ErrorCode.UNAUTHORIZED.code, "")
            }
        }

    override suspend fun getAllSavedFoodNames(): List<String> =
        db.foodDao().getAllFoodEntries().map { it.name }
}