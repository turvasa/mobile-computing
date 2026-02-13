package com.example.photodiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DatabaseViewModel(private val diaryItemDAO: DiaryItemDAO) : ViewModel() {


    // Automatically load all entries to memory
    val diaryItems: StateFlow<List<DiaryItem>> =
        diaryItemDAO.getAll()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )


    // Get single item
    fun getDiaryItemByID(id: Int) : StateFlow<DiaryItem?> =
        diaryItemDAO.findByID(id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null
            )



    fun addDiaryItem(diaryItem: DiaryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryItemDAO.insert(diaryItem)
        }
    }

    fun updateDiaryItem(updatedDiaryItem: DiaryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryItemDAO.update(updatedDiaryItem)
        }
    }

    fun deleteDiaryItem(diaryItem: DiaryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryItemDAO.delete(diaryItem)
        }
    }
}