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
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
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
     * @param defaultLatitude Default location's latitude.
     * @param defaultLongitude Default location's longitude.
     * @param isDefaultLocationUsed Whether only the default location is used or not.
     */
    fun loadWeather(context: Context, defaultLatitude: Float, defaultLongitude: Float, isDefaultLocationUsed: Boolean) {

        // Get the weather for the give location
        if (!isDefaultLocationUsed && areLocationPermissionsGranted(context)) {
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
            sendWithUserLocation(defaultLatitude, defaultLongitude)
        }

        // Get the weather from the default location
        else sendWeatherAPICall(defaultLatitude, defaultLongitude)
    }


    /**
     * Sends a Retrofit API call o fetch weather for the given location.
     *
     * @param latitude Latitude in degrees.
     * @param longitude Longitude in degrees.
     */
    private fun sendWeatherAPICall(latitude: Float, longitude: Float) {
        Log.d("Location", "Location: $latitude, $longitude")
        viewModelScope.launch {
            _weather.value = weatherApi.getCurrentWeather(
                lat = latitude,
                lon = longitude,
                apiKey = BuildConfig.WEATHER_API_KEY
            )
        }
    }




    // ------------
    // - Location -
    // ------------


    private var fusedLocationProviderClient: FusedLocationProviderClient? = null


    /**
     * Retrieves the current user location asynchronously.
     *
     * Source: https://medium.com/@munbonecci/how-to-get-your-location-in-jetpack-compose-f085031df4c1
     *
     * @param defaultLatitude Default location's latitude.
     * @param defaultLongitude Default location's longitude.
     */
    @SuppressLint("MissingPermission")
    private fun sendWithUserLocation(
        defaultLatitude: Float, defaultLongitude: Float,
        priority: Boolean = true
    ) {

        // Determine the accuracy priority based on the 'priority' parameter
        val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
        else Priority.PRIORITY_BALANCED_POWER_ACCURACY

        // Retrieve the last known location
        fusedLocationProviderClient?.getCurrentLocation(
            accuracy, CancellationTokenSource().token
        )?.addOnSuccessListener { location ->

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
                sendWeatherAPICall(defaultLatitude, defaultLongitude)
            }
        }
        ?.addOnFailureListener {
            Log.d("Location", "Listener failed")
            sendWeatherAPICall(defaultLatitude, defaultLongitude)
        }
    }

}
