package com.example.photodiary

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource




private var fusedLocationProviderClient: FusedLocationProviderClient? = null



fun getCurrentLocation(
    context: Context,
    onGetCurrentLocationSuccess: (Float, Float) -> Unit,
    onError: (Boolean) -> Unit
) {
    fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    // Check if location permissions are granted
    if (areLocationPermissionsGranted(context)) {
        getCurrentLocation(onGetCurrentLocationSuccess, onError)
    }
}




/**
 * Retrieves the current user location asynchronously.
 *
 * Source: https://medium.com/@munbonecci/how-to-get-your-location-in-jetpack-compose-f085031df4c1
 *
 * @param onGetCurrentLocationSuccess Callback function invoked when the current location is successfully retrieved.
 *        It provides a Pair representing latitude and longitude.
 * @param onError Callback function invoked when an error occurs while retrieving the current location.
 *        It provides the Exception that occurred.
 * @param priority Indicates the desired accuracy of the location retrieval. Default is high accuracy.
 *        If set to false, it uses balanced power accuracy.
 */
@SuppressLint("MissingPermission")
private fun getCurrentLocation(
    onGetCurrentLocationSuccess: (Float, Float) -> Unit,
    onError: (Boolean) -> Unit,
    priority: Boolean = true
) {
    // Determine the accuracy priority based on the 'priority' parameter
    val accuracy = if (priority) Priority.PRIORITY_HIGH_ACCURACY
    else Priority.PRIORITY_BALANCED_POWER_ACCURACY

    // Retrieve the current location asynchronously
    fusedLocationProviderClient?.getCurrentLocation(
        accuracy, CancellationTokenSource().token,
    )?.addOnSuccessListener { location ->
        location?.let {
            // If location is not null, invoke the success callback with latitude and longitude
            onGetCurrentLocationSuccess(it.latitude.toFloat(), it.longitude.toFloat())
            onError(false)
        }
    }?.addOnFailureListener {
        // If an error occurs, invoke the failure callback with the exception
        onError(true)
    }
}



/**
 * Checks if location permissions are granted.
 * Source: https://medium.com/@munbonecci/how-to-get-your-location-in-jetpack-compose-f085031df4c1
 *
 * @return true if both ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions are granted; false otherwise.
 */
fun areLocationPermissionsGranted(context: Context): Boolean {
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




// -----------------------
// - Location Permission -
// -----------------------


/**
 * Requests location permissions on a runtime.
 * Launches permission requests and triggers callbacks depending
 * on whether all required permissions are granted or not.
 *
 * Source: https://medium.com/@munbonecci/how-to-get-your-location-in-jetpack-compose-f085031df4c1
 *
 * @param onPermissionGranted Callback invoked when permissions are granted.
 * @param onPermissionDenied Callback invoked when permissions are denied.
 */
@Composable
fun rememberLocationPermissionLauncher(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
): () -> Unit {
    // 1. Create a stateful launcher using rememberLauncherForActivityResult
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        // 2. Check if all requested permissions are granted
        val arePermissionsGranted = permissionsMap.values.reduce { acc, next ->
            acc && next
        }

        // 3. Invoke the appropriate callback based on the permission result
        if (arePermissionsGranted) {
            onPermissionGranted.invoke()
        } else {
            onPermissionDenied.invoke()
        }
    }

    // 4. Launch the permission request on composition
    return {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}
