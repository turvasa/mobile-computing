package com.example.photodiary

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * Factory class for creating [WeatherViewModel] instance.
 */
class WeatherViewModelFactory : ViewModelProvider.Factory {


    /**
     * Creates an instance of [WeatherViewModel] using the given DAO
     *
     * @return A new instance of [WeatherViewModel].
     */
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return WeatherViewModel() as T
    }

}