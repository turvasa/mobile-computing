package com.example.photodiary

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow


/**
 * DAO (Data Access Object) interface for performing database operations on [DiaryItem] entities.
 * Provides INSERT, UPDATE, GET and DELETE methods.
 */
@Dao
interface DiaryItemDAO {


    /**
     * Gets a [Flow] containing all diary items in the database in descending order by ID.
     *
     * @return [Flow] of list of DiaryItem.
     */
    @Query("SELECT * FROM DiaryItem ORDER BY id DESC")
    fun getAll(): Flow<List<DiaryItem>>


    /**
     * Gets a [Flow] containing a single diary item by the given ID.
     *
     * @param id ID of the diary item to retrieve.
     * @return [Flow] emitting the DiaryItem or null if not found.
     */
    @Query("SELECT * FROM DiaryItem WHERE id = :id")
    fun findByID(id: Int): Flow<DiaryItem?>


    /**
     * Inserts the given diary item into the database.
     *
     * @param diaryItem DiaryItem object to be inserted.
     */
    @Insert
    suspend fun insert(diaryItem: DiaryItem)


    /**
     * Updates the given diary item in the database.
     *
     * @param updatedItem DiaryItem object containing updated values.
     */
    @Update
    suspend fun update(updatedItem: DiaryItem)


    /**
     * Deletes the given diary item from the database.
     *
     * @param diaryItem DiaryItem object to be deleted.
     */
    @Delete
    suspend fun delete(diaryItem: DiaryItem)
}