package com.example.core.model.converters

import com.example.api.model.UserAverageCaloriesDTO
import com.example.core.model.UserAverageCalories

internal fun UserAverageCaloriesDTO.toModel() =
    if (username != null && count != null)
        UserAverageCalories(username!!, count!!.toInt())
    else null