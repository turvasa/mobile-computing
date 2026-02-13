package com.example.photodiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class DatabaseViewModelFactory(private val diaryItemDAO: DiaryItemDAO) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DatabaseViewModel(diaryItemDAO) as T
    }

}