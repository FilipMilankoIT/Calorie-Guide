package com.example.core.model.converters.responses

import com.example.api.model.UserDTO
import com.example.api.model.responses.GetUsersResponseDTO
import com.example.core.storage.room.UserEntity

internal fun GetUsersResponseDTO.toEntityList(): List<UserEntity> =
    this.users?.mapNotNull { it.toEntity() } ?: listOf()

private fun UserDTO.toEntity(): UserEntity? =
    if (this.username != null && this.role != null && this.dailyCalorieLimit != null)
        UserEntity(this.username!!, this.role!!, this.dailyCalorieLimit!!, this.firstName,
            this.lastName, this.gender, this.birthday)
    else null