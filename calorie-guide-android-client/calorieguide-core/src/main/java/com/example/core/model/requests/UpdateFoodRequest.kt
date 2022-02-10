package com.example.core.model.requests

import androidx.annotation.Keep

@Keep
data class UpdateFoodRequest(
    val name: String,
    val timestamp: Long,
    val calories: Int
    )