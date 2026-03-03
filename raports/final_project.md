# Final Project - Tatu Tallavaara, 2208317


## Features




### New entries to the database

While in the HW4 I stored optional weather information with the necessary title, (optional) description and image name, 
I now also have weatherIcon (ID), latitude and longitude:

```kotlin
@Entity
data class DiaryItem(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,

    @ColumnInfo(name = "imageName") val imageName: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String?,

    @ColumnInfo(name = "temperature") val temperature: Double?,
    @ColumnInfo(name = "weather") val weather: String?,
    @ColumnInfo(name = "weatherIcon") val weatherIcon: String?,

    @ColumnInfo(name = "latitude") val latitude: Float,
    @ColumnInfo(name = "longitude") val longitude: Float,
)
```




### Location


#### Sensor

The following permissions are added to the manifest:

```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

And to build.gradle (app):

```kotlin
implementation("com.google.android.gms:play-services-location:21.3.0")
implementation("com.google.accompanist:accompanist-permissions:0.37.3")
```

I ask the permission in the following way (in MainActivit before setting the content):
```kotlin
val requestPermissionLauncher = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
) { }

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    requestPermissionLauncher.launch(arrayOf(
        Manifest.permission.POST_NOTIFICATIONS,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    ))
}
```

I found a good [source]("https://medium.com/@munbonecci/how-to-get-your-location-in-jetpack-compose-f085031df4c1") for 
location tracking (mentioned also in the code docs). It gave me everything needed, but I had to make some edits to make 
it sufficient for my purposes. The weather API uses last known user location or default location, if location permissions 
aren't granted or the found location is null, the default location is set to be Oulu. The default location can be changed 
from settings, where the current location data is used and it is stored to the shared preferences:

```kotlin
val preferences = getSharedPreferences("settings", Context.MODE_PRIVATE)

// Get with default value of 65.01236F, if the preference don't exist yet
val latitudePreference = preferences.getFloat("defaultLatitude", 65.01236F)

// Set
preferences.edit {
    putFloat("defaultLatitude", 60.192059F)
}
```

Everything from setting are saved to the shared preferences, so the values stay the same even after app is closed. 
The preferences use hard drive, so I also have each of these settings as variable (in RAM) to keep the app flow smooth.


#### Name

The location sensor gives the latitude and the longitude of the location, but the user won't do anything with that 
information alone, so I implemented also a Geocode.GeocodeListener for getting the location names (city and country). 
I first created a Geocode object and used it to get the location names with my custom location name listener:

```kotlin
val geocoder = Geocoder(context, locale) // Aplication context and the used language as Locale (e.g. English would be Locale.ENGLISH)
geocoder.getFromLocation(
    latitude.toDouble(), longitude.toDouble(), // Location info
    maxResults = 1, // 1 result is enough (no idea where larger values are used) 
    listener = LocationNameListener(cityCallback, countryCallback) // My custom GeocodeListener
)
```

My custom listener:

```kotlin
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

        // Get info from the first item
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
```




### Runtime permission check and granting

The settings can be used to change the default location, and runtime permission ask is utilized here. In the following way:

```kotlin
// The used method is from the same source from above and it handles the permission request.
val launchPermissionRequest = rememberLocationPermissionLauncher(
    {
        getCurrentLocation(context, changeDefaultLocation, toggleError)
    },
    {
        toggleError(true)
    }
)


// This is passed to button as on click event
if (areLocationPermissionsGranted(context)) {
    getCurrentLocation(
        context,
        changeDefaultLocation, // Changes the latitude and the longitude to the preferences and also the corresponding variables. 
        toggleError // Toggles isError variable. Used only for giving the user info, if something went wrong while getting the location.
    )
}
else {
    launchPermissionRequest() 
}
```

Basically if location permission is granted, get the location. If not, ask for the permission. If they are granted then get 
the location and if not, show the error message to the user (why would the user try to add location, if he don't want to 
give the location permissions).  




### Camera

I used the default camera application of the device (not CameraX, which is much harder to setup). My application is for 
storing photos to the diary and not a camera application, so while this approach might not get full points, it was the 
best way in this situation. The camera app can be opened in the following way:

```kotlin
// Temporary URI for the camera app (the app need an URI for the to be taken photo)
var tempUri by remember { mutableStateOf<Uri?>(null) }

// Setup camera launcher
val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture()
) { accepted ->

    // Save the uri, if image was accepted
    if (accepted) {
        toggleImageUri(tempUri) // Save the URI to the 'global' variable for saving
    }
}

// On click event for the camera button (my application)
val onClickEvent = { getTakeImageEvent(context, cameraLauncher) { tempUri = it } }


// On click event for opening the camera app and capturing the image given to the button.
private fun getTakeImageEvent(
    context: Context, cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    toggleTempImageUri: (Uri?) -> Unit
) {

    // Create a temporary file in the cache (for the camera app)
    val tempFile = File(
        context.cacheDir,
        "temp_${System.currentTimeMillis()}.jpg"
    )

    // Reserve a space for the next image from the cache
    val tempUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        tempFile
    )
    toggleTempImageUri(tempUri) // Toggle the to be taken image to the temporary URI 

    // Launch the camera app
    cameraLauncher.launch(tempUri)
}

```




### Other features

Here are some other features that might give some points to the grading.


#### Settings

1. Notification time change on run time with an intercative clock.

```kotlin
// State for the clock
val timePickerState = rememberTimePickerState(
    initialHour = hour,
    initialMinute = minutes,
    is24Hour = true
)

// Display the clock
TimePicker(timePickerState)

// On click event for accepting the clock's time
{ onConfirm(timePickerState.hour, timePickerState.minute) }
```

2. Two languages: Finnish and English


#### Diary item edit and delete

The diary items can be selected from the HOME tab by clicking one, but by long press the item can be edited. The edit 
includes the title and description input fields. In this same view the item can also be deleted entirely.





## AI usage

ChatGPT used for documentation and as a coaching tool like in homeworks, so no copy-paste code from AI.



## Links
[GitHub]("https://github.com/turvasa/mobile-computing")
