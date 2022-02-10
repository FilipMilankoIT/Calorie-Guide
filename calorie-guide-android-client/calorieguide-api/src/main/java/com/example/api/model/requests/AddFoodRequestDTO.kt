package com.example.api.model.requests

import androidx.annotation.Keep

@Keep
data class AddFoodRequestDTO(
    val username: String?,
    val name: String,
    val timestamp: Long,
    val calories: Int
    )