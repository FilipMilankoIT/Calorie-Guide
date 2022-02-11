package com.example.api.model.responses

import androidx.annotation.Keep
import com.example.api.model.UserDTO

@Keep
data class GetUsersResponseDTO(val users: List<UserDTO>?)
