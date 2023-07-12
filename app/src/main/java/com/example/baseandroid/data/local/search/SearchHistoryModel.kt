package com.example.baseandroid.data.local.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SearchHistory")
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "key_search") val KeySearch: String
)