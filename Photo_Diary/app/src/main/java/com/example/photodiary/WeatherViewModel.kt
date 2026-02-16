package com.example.photodiary

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.flow.StateFlow




class WeatherViewModel : ViewModel() {

    private val _weather = MutableStateFlow<Weather?>(null)
    var weather: StateFlow<Weather?> = _weather


    // Retrofit and API defining
    private val weatherApiUrl = "https://api.openweathermap.org/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(weatherApiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val weatherApi: WeatherAPI = retrofit.create(WeatherAPI::class.java)


    fun loadWeather() {
        println(BuildConfig.WEATHER_API_KEY)
        viewModelScope.launch {
            _weather.value = weatherApi.getCurrentWeather(
                apiKey = BuildConfig.WEATHER_API_KEY
            )
        }
    }

}