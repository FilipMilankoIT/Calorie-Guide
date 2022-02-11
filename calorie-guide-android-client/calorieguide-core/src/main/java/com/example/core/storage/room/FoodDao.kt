package com.example.core.storage.room

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FoodDao {

    @Query("SELECT * FROM food_table WHERE username = :username AND timestamp >= :from AND  timestamp <= :to ORDER BY timestamp DESC")
    fun getFoodEntriesByTimeRange(username: String, from: Long, to: Long): DataSource.Factory<Int, FoodEntity>

    @Query("SELECT SUM(calories) FROM food_table WHERE username = :username AND timestamp >= :from AND  timestamp <= :to")
    suspend fun getCalorieSumByTimeRange(username: String, from: Long, to: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(food: FoodEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(food: List<FoodEntity>)

    @Query("UPDATE food_table SET name = :name, timestamp = :timestamp, calories = :calories WHERE id = :id")
    suspend fun updateFood(id: String, name: String, timestamp: Long, calories: Int)

    @Query("DELETE FROM food_table  WHERE id = :id")
    suspend fun deleteFood(id: String)

    @Query("DELETE FROM food_table WHERE username = :username AND timestamp >= :from AND  timestamp <= :to")
    suspend fun deleteFoodEntriesByTimeRange(username: String, from: Long, to: Long)

    @Query("DELETE FROM food_table")
    suspend fun deleteAll()
}