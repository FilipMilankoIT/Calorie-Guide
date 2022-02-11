package com.example.core.model.responses

import androidx.annotation.Keep
import com.example.core.model.User

@Keep
data class GetUsersResponse(val users: List<User>)
