package com.example.photodiary

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryItemDAO {

    @Query("SELECT * FROM DiaryItem ORDER BY id DESC")
    fun getAll(): Flow<List<DiaryItem>>

    @Query("SELECT * FROM DiaryItem WHERE id = :id")
    fun findByID(id: Int): Flow<DiaryItem?>

    @Insert
    suspend fun insert(diaryItem: DiaryItem)

    @Update
    suspend fun update(updatedItem: DiaryItem)

    @Delete
    suspend fun delete(diaryItem: DiaryItem)
}