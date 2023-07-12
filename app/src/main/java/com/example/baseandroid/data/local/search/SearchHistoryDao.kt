package com.example.baseandroid.data.local.search

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SearchHistoryDao {
    @Query("SELECT * FROM SearchHistory")
    suspend fun getAll(): List<SearchHistoryEntity>

    @Query("SELECT * FROM SearchHistory WHERE uid IN (:userIds)")
    suspend fun loadAllByIds(userIds: IntArray): List<SearchHistoryEntity>

    @Query("SELECT * FROM SearchHistory WHERE key_search LIKE '%' || :first || '%' ORDER BY uid DESC LIMIT 3")
    suspend fun findByName(first: String): List<SearchHistoryEntity>

    @Query("SELECT * FROM SearchHistory WHERE key_search=:name")
    suspend fun findByKey(name: String): List<SearchHistoryEntity>

    @Insert
    suspend fun insertAll(vararg users: SearchHistoryEntity)

    @Delete
    suspend fun delete(user: SearchHistoryEntity)
}