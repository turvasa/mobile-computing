package com.example.photodiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * Factory class for creating [DatabaseViewModel] instances with a custom DAO
 *
 * @param diaryItemDAO DAO used by the [DatabaseViewModel].
 */
class DatabaseViewModelFactory(private val diaryItemDAO: DiaryItemDAO) : ViewModelProvider.Factory {


    /**
     * Creates an instance of [DatabaseViewModel] using the given DAO
     *
     * @param modelClass The ViewModel class to create.
     * @return A new instance of [DatabaseViewModel] cast to the requested type.
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DatabaseViewModel(diaryItemDAO) as T
    }

}