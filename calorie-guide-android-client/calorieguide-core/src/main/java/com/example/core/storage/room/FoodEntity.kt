package com.example.core.storage.room

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.core.model.Food

@Keep
@Entity(tableName = "food_table")
data class FoodEntity(@PrimaryKey val id: String, val username: String, val name : String,
                      val timestamp : Long, val calories : Int)

fun FoodEntity.toModel() = Food(this.id, this.username, this.name, this.timestamp, this.calories)

fun Food.toEntry() = FoodEntity(this.id, this.username, this.name, this.timestamp, this.calories)