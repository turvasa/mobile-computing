package com.example.photodiary

import retrofit2.http.Query
import retrofit2.http.GET

interface WeatherAPI {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double = 65.01236,
        @Query("lon") lon: Double = 25.46816,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): Weather

}
