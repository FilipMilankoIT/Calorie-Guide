package com.example.api.model.requests

import androidx.annotation.Keep

@Keep
data class GetFoodListRequest(val username: String?, val from: Long?, val to: Long?)
