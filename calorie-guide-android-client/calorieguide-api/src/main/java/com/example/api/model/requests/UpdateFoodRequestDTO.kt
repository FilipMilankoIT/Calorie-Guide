package com.example.api.model.requests

import androidx.annotation.Keep

@Keep
data class UpdateFoodRequestDTO(
    val name: String,
    val timestamp: Long,
    val calories: Int
    )