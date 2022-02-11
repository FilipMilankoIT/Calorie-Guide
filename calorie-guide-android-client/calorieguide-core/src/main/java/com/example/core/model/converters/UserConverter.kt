package com.example.core.model.converters

import com.example.api.model.UserDTO
import com.example.core.model.DEFAULT_DAILY_CALORIE_LIMIT
import com.example.core.model.User
import com.example.core.model.UserRole

internal fun UserDTO.toModel() =
    if (username != null && role != null)
        User(
            username!!,
            UserRole.from(role),
            dailyCalorieLimit ?: DEFAULT_DAILY_CALORIE_LIMIT,
            firstName, lastName, gender, birthday
        )
    else null