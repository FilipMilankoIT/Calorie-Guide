package com.example.core.model.converters.reqests

import com.example.api.model.requests.UpdateFoodRequestDTO
import com.example.core.model.requests.UpdateFoodRequest

internal fun UpdateFoodRequest.toDTO() = UpdateFoodRequestDTO(name, timestamp, calories)