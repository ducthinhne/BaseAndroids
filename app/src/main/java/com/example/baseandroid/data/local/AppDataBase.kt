package com.example.baseandroid.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.baseandroid.data.local.search.SearchHistoryDao
import com.example.baseandroid.data.local.search.SearchHistoryEntity

@Database(entities = [SearchHistoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): SearchHistoryDao
}