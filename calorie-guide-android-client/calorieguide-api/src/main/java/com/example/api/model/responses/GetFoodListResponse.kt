package com.example.api.model.responses

import androidx.annotation.Keep
import com.example.api.model.FoodDTO

@Keep
data class GetFoodListResponse(val items: List<FoodDTO>?)
