package com.example.core.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class Food(val id: String, val username: String, val name : String, val timestamp : Long,
                val calories : Int): Parcelable
