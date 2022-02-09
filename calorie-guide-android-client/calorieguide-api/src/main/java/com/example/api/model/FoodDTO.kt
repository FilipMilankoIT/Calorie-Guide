package com.example.api.model

import androidx.annotation.Keep

@Keep
data class FoodDTO(
    val id: String?,
    val username: String?,
    val name : String?,
    val timestamp : Long?,
    val calories : Int?
    )
