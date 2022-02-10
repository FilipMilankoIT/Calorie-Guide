package com.example.core.model.converters.reqests

import com.example.api.model.requests.AddFoodRequestDTO
import com.example.core.model.requests.AddFoodRequest

internal fun AddFoodRequest.toDTO() = AddFoodRequestDTO(username, name, timestamp, calories)