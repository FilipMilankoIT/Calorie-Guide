package com.example.api.model

import androidx.annotation.Keep

@Keep
data class UserDTO(
    val username: String?,
    val role : String?,
    val dailyCalorieLimit: Int?,
    val firstName: String?,
    val lastName: String?,
    val gender: String?,
    val birthday: Long?
    )
