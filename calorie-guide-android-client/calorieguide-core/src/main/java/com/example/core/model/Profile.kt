package com.example.core.model

import androidx.annotation.Keep

@Keep
data class Profile(
    val username: String,
    val role: UserRole,
    val dailyCalorieLimit: Int,
    val firstName: String?,
    val lastName: String?,
    val gender: Gender?,
    val birthday: Long?
    )

const val DEFAULT_DAILY_CALORIE_LIMIT = 2100