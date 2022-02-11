package com.example.core.storage.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FoodEntity::class, UserEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun foodDao(): FoodDao
    abstract fun userDao(): UserDao
}