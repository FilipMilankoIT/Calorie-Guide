package com.example.core.model.converters.responses

import com.example.api.model.FoodDTO
import com.example.api.model.responses.GetFoodListResponseDTO
import com.example.core.storage.room.FoodEntity

internal fun GetFoodListResponseDTO.toEntityList(): List<FoodEntity> =
    this.items?.mapNotNull { it.toEntity() } ?: listOf()

private fun FoodDTO.toEntity(): FoodEntity? =
    if (this.id != null && this.username != null && this.name != null && this.timestamp != null
        && this.calories != null)
        FoodEntity(this.id!!, this.username!!, this.name!!, this.timestamp!!, this.calories!!)
    else null