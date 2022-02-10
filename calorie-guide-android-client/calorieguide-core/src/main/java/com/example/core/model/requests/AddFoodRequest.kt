package com.example.core.model.requests

import androidx.annotation.Keep

@Keep
data class AddFoodRequest(
    val username: String?,
    val name: String,
    val timestamp: Long,
    val calories: Int
    )