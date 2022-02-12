package com.example.core.storage.room

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.model.Gender
import com.example.core.model.User
import com.example.core.model.UserRole

@Keep
@Entity(tableName = "user_table")
data class UserEntity(@PrimaryKey val username: String, val role: String,
                      val dailyCalorieLimit: Int, val firstName: String?,
                      val lastName: String?, val gender: String?, val birthday: Long?)

fun UserEntity.toModel() = User(this.username, UserRole.from(this.role), this.dailyCalorieLimit,
    this.firstName, this.lastName, Gender.from(this.gender), this.birthday)

fun User.toEntry() = UserEntity(this.username, this.role.value, this.dailyCalorieLimit,
    this.firstName, this.lastName, this.gender?.value, this.birthday)