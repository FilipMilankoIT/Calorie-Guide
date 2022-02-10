package com.example.core.model.converters

import com.example.api.model.FoodDTO
import com.example.core.model.Food

internal fun FoodDTO.toModel() =
    if (id != null && username != null && name != null && timestamp != null && calories != null)
        Food(id!!, username!!, name!!, timestamp!!, calories!!)
    else null