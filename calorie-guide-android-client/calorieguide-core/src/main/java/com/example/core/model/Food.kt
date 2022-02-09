package com.example.core.model

import androidx.annotation.Keep

@Keep
data class Food(val id: String, val username: String, val name : String, val timestamp : Long,
                val calories : Int)
