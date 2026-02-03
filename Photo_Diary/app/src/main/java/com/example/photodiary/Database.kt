package com.example.photodiary

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DiaryItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diaryItemDao(): DiaryItemDAO
}

