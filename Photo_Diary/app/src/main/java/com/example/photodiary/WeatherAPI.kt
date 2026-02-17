package com.example.photodiary

import retrofit2.http.Query
import retrofit2.http.GET


/**
 * Retrofit interface for fetching [Weather] data from OpenWeatherMap API
 */
interface WeatherAPI {


    /**
     * Fetches the current weather for the given location.
     *
     * @param lat Latitude of the location
     * @param lon Longitude of the location
     * @param units Unit system for temperature. Defaults to "metric" (°C)
     * @param apiKey OpenWeatherMap API key
     * @return [Weather] object containing weather details
     */
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query("units") units: String = "metric",
        @Query("appid") apiKey: String
    ): Weather

}
