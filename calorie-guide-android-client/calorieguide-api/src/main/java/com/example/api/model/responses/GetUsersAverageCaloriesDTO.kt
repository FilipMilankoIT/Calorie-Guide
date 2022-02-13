package com.example.api.model.responses

import androidx.annotation.Keep
import com.example.api.model.UserAverageCaloriesDTO

@Keep
data class GetUsersAverageCaloriesDTO(val items: List<UserAverageCaloriesDTO>?)
