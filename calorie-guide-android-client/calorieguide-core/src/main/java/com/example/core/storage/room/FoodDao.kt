package com.example.core.storage.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FoodDao {

    @Query("SELECT * FROM food_table WHERE username = :username AND timestamp >= :from AND  timestamp <= :to ORDER BY timestamp DESC")
    fun getFoodEntriesInPeriod(username: String, from: Long, to: Long): DataSource.Factory<Int, FoodEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: FoodEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(food: List<FoodEntity>)
}