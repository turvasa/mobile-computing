package com.example.photodiary

import android.content.Context
import android.location.Address
import android.location.Geocoder


/**
 * Helper class to handle the location listening event.
 * Implements [Geocoder.GeocodeListener] for the location transform
 * from latitude and longitude to the location names.
 *
 * @param cityCallback Callback event for getting the city name.
 * @param countryCallback Callback event for getting the country name.
 */
private class LocationNameListener(
    private val cityCallback: (String?) -> Unit,
    private val countryCallback: (String?) -> Unit
) : Geocoder.GeocodeListener {


    override fun onGeocode(addresses: List<Address>) {
        // No address found
        if (addresses.isEmpty()) {
            cityCallback(null)
            countryCallback(null)
            return
        }

        // Get info
        val cityName = addresses[0].locality ?: addresses[0].subAdminArea ?: "Unknown city"
        val countryName = addresses[0].countryName ?: "Unknown country"

        // Set the info to the callbacks
        cityCallback(cityName)
        countryCallback(countryName)
    }


    override fun onError(errorMessage: String?) {
        cityCallback(null)
        countryCallback(null)
    }

}


/**
 * Gives the location names (city and country) from the given latitude and longitude.
 * The names are assigned with the corresponding callback events.
 *
 * @param latitude Location's latitude
 * @param longitude Location's longitude
 * @param cityCallback Callback event for getting the city name.
 * @param countryCallback Callback event for getting the country name.
 */
fun getLocationName(
    latitude: Float, longitude: Float, context: Context,
    cityCallback: (String?) -> Unit, countryCallback: (String?) -> Unit
) {
    val geocoder = Geocoder(context)
    geocoder.getFromLocation(
        latitude.toDouble(), longitude.toDouble(), 1,
        LocationNameListener(cityCallback, countryCallback)
    )
}

