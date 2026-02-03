package com.example.photodiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class DatabaseMethods(private val diaryItemDAO: DiaryItemDAO) : ViewModel() {

    val diaryItems = MutableStateFlow<List<DiaryItem>>(emptyList())


    fun loadDiaryItems() {
        viewModelScope.launch(Dispatchers.IO) {
            diaryItems.value = diaryItemDAO.getAll()
        }
    }

    fun findByID(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryItems.value = diaryItemDAO.findByID(id)
        }
    }

    fun addDiaryItem(diaryItem: DiaryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryItemDAO.insert(diaryItem)
            loadDiaryItems()
        }
    }

    fun updateDiaryItem(updatedDiaryItem: DiaryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryItemDAO.update(updatedDiaryItem)
            loadDiaryItems()
        }
    }

    fun deleteDiaryItem(diaryItem: DiaryItem) {
        viewModelScope.launch(Dispatchers.IO) {
            diaryItemDAO.delete(diaryItem)
        }
    }
}