package com.example.core.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class User(
    val username: String,
    val role: UserRole,
    val dailyCalorieLimit: Int,
    val firstName: String?,
    val lastName: String?,
    val gender: String?,
    val birthday: Long?
    ): Parcelable
