package com.example.photodiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


/**
 * [ViewModel] for interacting with the Room database.
 * Provides [StateFlow] streams of diary items and methods to
 * ADD, UPDATE or DELETE diary entries from the database.
 *
 * @param diaryItemDAO DAO for accessing DiaryItem entities.
 */
class DatabaseViewModel(private val diaryItemDAO: DiaryItemDAO) : ViewModel() {


    /**
     * [StateFlow] containing all diary entries loaded from the database.
     * Automatically updates when when the database changes.
     */
    val diaryItems: StateFlow<List<DiaryItem>> =
        diaryItemDAO.getAll()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )


    /**
     * Gets a StateFlow containing the searched diary item by the given ID.
     *
     * @param id ID of the diary item.
     * @return [StateFlow] containing the DiaryItem or null if not found.
     */
    fun getDiaryItemByID(id: Int) : StateFlow<DiaryItem?> =
        diaryItemDAO.findByID(id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )


    /**
     * Inserts the given new diary item into the database asynchronously.
     *
     * @param diaryItem DiaryItem object to be added.
     */
    fun addDiaryItem(diaryItem: DiaryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryItemDAO.insert(diaryItem)
        }
    }


    /**
     * Updates the given diary item in the database asynchronously.
     * Nothing is done, if the given diary item don't exist in the database.
     *
     * @param updatedDiaryItem DiaryItem object containing updated values.
     */
    fun updateDiaryItem(updatedDiaryItem: DiaryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryItemDAO.update(updatedDiaryItem)
        }
    }


    /**
     * Deletes the given diary item from the database asynchronously.
     * Nothing is done, if the given diary item don't exist in the database.
     *
     * @param diaryItem DiaryItem object to be deleted.
     */
    fun deleteDiaryItem(diaryItem: DiaryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryItemDAO.delete(diaryItem)
        }
    }

}
