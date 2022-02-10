package com.example.core.model.converters.reqests

import com.example.api.model.requests.RegisterRequestDTO
import com.example.core.model.requests.RegisterRequest

internal fun RegisterRequest.toDTO() = RegisterRequestDTO(username, password, firstName, lastName,
    gender, birthday)