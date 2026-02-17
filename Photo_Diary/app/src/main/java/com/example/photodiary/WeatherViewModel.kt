package com.example.photodiary

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlinx.coroutines.flow.StateFlow


/**
 * [ViewModel] responsible for fetching and holding the current [Weather] data.
 */
class WeatherViewModel : ViewModel() {

    /** Internal mutable weather state. */
    private val _weather = MutableStateFlow<Weather?>(null)

    /** Publicly exposed weather as [StateFlow]. */
    val weather: StateFlow<Weather?> = _weather




    // ----------------------
    // - Retrofit API Setup -
    // ----------------------
    private val weatherApiUrl = "https://api.openweathermap.org/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(weatherApiUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val weatherApi: WeatherAPI = retrofit.create(WeatherAPI::class.java)




    // --------------
    // - Public API -
    // --------------


    /**
     * Loads weather either using user location (if permission granted)
     * or default location.
     *
     * @param context The context used for location services.
     * @param preferences SharedPreferences for default location values.
     */
    fun loadWeather(context: Context, preferences: SharedPreferences) {

        // Get the weather for the give location
        if (areLocationPermissionsGranted(context)) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            sendWithLastUserLocation(preferences)
        }

        // Get the weather from the default location (Oulu)
        else sendDefaultLocationAPICall(preferences)
    }


    /**
     * Sends a Retrofit API call o fetch weather for the given location.
     *
     * @param latitude Latitude in degrees.
     * @param longitude Longitude in degrees.
     */
    private fun sendWeatherAPICall(latitude: Float, longitude: Float) {
        viewModelScope.launch {
            _weather.value = weatherApi.getCurrentWeather(
                lat = latitude,
                lon = longitude,
                apiKey = BuildConfig.WEATHER_API_KEY
            )
        }
    }


    /**
     * Sends a Retrofit API call o fetch weather for the default location stored in preferences.
     */
    private fun sendDefaultLocationAPICall(preferences: SharedPreferences) {

        Log.d("Location", "Location not found")

        val latitude = preferences.getFloat("default_latitude", 65.01236F)
        val longitude = preferences.getFloat("default_longitude", 25.46816F)
        sendWeatherAPICall(latitude, longitude)
    }




    // ------------
    // - Location -
    // ------------

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null


    /**
     * Sends the last known user location asynchronously to the Weather API.
     * Source: https://medium.com/@munbonecci/how-to-get-your-location-in-jetpack-compose-f085031df4c1
     *
     * @param preferences SharedPreferences for default location fallback.
     */
    @SuppressLint("MissingPermission")
    private fun sendWithLastUserLocation(preferences: SharedPreferences) {
        // Retrieve the last known location
        fusedLocationProviderClient?.lastLocation
            ?.addOnSuccessListener { location ->

                // Invoke the API call with the location
                if (location != null) {

                    Log.d("Location", "Location found")

                    val latitude = location.latitude.toFloat()
                    val longitude = location.longitude.toFloat()

                    Log.d("Location", "Latitude: %.6f, Longitude: %.6f".format(latitude, longitude))

                    sendWeatherAPICall(latitude, longitude)
                }

                // Invoke the default location API call
                else {
                    Log.d("Location", "Null Location")
                    sendDefaultLocationAPICall(preferences)
                }
            }
            ?.addOnFailureListener {
                Log.d("Location", "Listener failed")
                sendDefaultLocationAPICall(preferences)
            }
    }


    /**
     * Checks if location permissions are granted.
     * Source: https://medium.com/@munbonecci/how-to-get-your-location-in-jetpack-compose-f085031df4c1
     *
     * @return true if both ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions are granted; false otherwise.
     */
    private fun areLocationPermissionsGranted(context: Context): Boolean {
        return (
                ActivityCompat.checkSelfPermission(
                    context, Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
                        &&
                        ActivityCompat.checkSelfPermission(
                            context, Manifest.permission.ACCESS_COARSE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                )
    }
}
