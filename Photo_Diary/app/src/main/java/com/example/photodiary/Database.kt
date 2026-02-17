package com.example.photodiary

import androidx.room.Database
import androidx.room.RoomDatabase


/**
 * Room database for the application.
 * Holds the [DiaryItem] entity and provides access to its DAO
 */
@Database(entities = [DiaryItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun diaryItemDao(): DiaryItemDAO
}

