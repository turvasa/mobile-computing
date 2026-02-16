# HW4 - Tatu Tallavaara, 2208317


## How do you check or prompt for notification permission?

First I added the following line to the 'AndroidManifest.xml' to declare, that I want to use that permission 
```kotlin
<uses-permission android:name="android.permission.POST_NOTIFICATIONS"/>
```

In the 'MainActivity' class and within the 'onCreate()' function, I asked the run time permission to send notifications:
```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
}
```

Apparently the manifest declaration is enough with Android 12 or lower, so the run time permission is only needed, if my build's Android version is 13 (Tiramisu) or higher.



## How did you get your app to trigger a notification while it’s not in the foreground?

I used WorkManager to move the notification task to the Android system, which will run the task even in foreground. 
```kotlin
WorkManager.getInstance(context).enqueueUniqueWork(
    uniqueWorkName = "daily_notification_work",
    existingWorkPolicy = ExistingWorkPolicy.REPLACE,
    request = dailyWorkRequest
)
```

I defined my request to be daily at given time:
```kotlin
val delay = scheduleTime.timeInMillis - currentTime.timeInMillis                // Time (delay) until the notification
val dailyWorkRequest = OneTimeWorkRequestBuilder<DailyNotificationWorker>()
    .setInitialDelay(delay, TimeUnit.MILLISECONDS)
    .addTag("daily_notification")
    .build()
```



## How did you make the notification interactable?

When building the notification, the 'pendingIntent' determines, what happens after clicking the notification banner:
```kotlin
val intent = Intent(context, MainActivity::class.java).apply {
    action = Intent.ACTION_VIEW
    data = "photodiary://add".toUri()
}
val pendingIntent = android.app.PendingIntent.getActivity(
    context, 0, intent,
    android.app.PendingIntent.FLAG_IMMUTABLE
)

// Build the notification
val notification = NotificationCompat.Builder(context, channelId)
    .setSmallIcon(R.drawable.icon_notification)
    .setContentTitle(title)
    .setContentText(message)
    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    .setContentIntent(pendingIntent)
    .setAutoCancel(true)
```

The 'intent' tells, what is the action for the 'pendingIntent'. In my case, it leads to the ADD destination due to the added deeepLink:
```kotlin
// Add
composable(
    AppDestinations.ADD.route,
    deepLinks = listOf(navDeepLink { uriPattern = "photodiary://add" }) // Uri link for notifications
) {
    AddNewCard(isDarkMode, appColors, appLanguage, weatherViewModel, databaseViewModel)
}
```



## What API are you using and how are you reading its response?

I use [OpenWeather]("https://openweathermap.org/")'s API for weather information from the given location (location tracking will be added later, so now Oulu's cordinates are used). The basic logic uses 'Retrofit' to fecth the API data from the given URL
```kotlin
private val weatherApiUrl = "https://api.openweathermap.org/"   // Generic URL
private val retrofit = Retrofit.Builder()
    .baseUrl(weatherApiUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
private val weatherApi: WeatherAPI = retrofit.create(WeatherAPI::class.java)


fun loadWeather() {
    println(BuildConfig.WEATHER_API_KEY)
    viewModelScope.launch {                                     // The class implements ViewModel interface
        _weather.value = weatherApi.getCurrentWeather(
            apiKey = BuildConfig.WEATHER_API_KEY                // API key is defined in 'local.prpperties'
        )
    }
}
```

```kotlin
interface WeatherAPI {
    @GET("data/2.5/weather")                        // Specific URL
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double = 65.01236,       // Latitude of Oulu
        @Query("lon") lon: Double = 25.46816,       // Longitude of Oulu
        @Query("units") units: String = "metric",   // Get temperature as Celsius
        @Query("appid") apiKey: String
    ): Weather
}

```

The Android Studio and Retrofit is quite high-level, so I really couldn't see the response JSON easily, so I just used cURL in terminal to detect the response pattern (The API docs with examples can be found from https://openweathermap.org/current?collection=current_forecast. I just now realised that the example response is there as well 😿):
```zsh
curl -v "https://api.openweathermap.org/data/2.5/weather?lat=65.01236&lon=25.46816&units=metric&appid=<API key>
```

Now I just needed to make data class Weather to imitate the response:
```kotlin
data class Weather(
    val main: Main,
    val weather: List<WeatherDescription>,
    val name: String            // Location name
) {...}

data class Main(
    val temp: Double,           // Temperature
)

data class WeatherDescription(
    val main: String,           // Short main description of the weather 
    val description: String     // Long description of the weather (not needed currently)
)
```



## What are you using the API response for?

The weather information is used as one of the diary entry details for the image. I'm planning to have entry sorting on the HOME page with day, temperature, location and title.



## AI usage

This week gave me little hardships, so I had to use ChatGPT as a teaching/learning tool. Non of the code are copy-paste from AI, but I have had conversations about code and designs with the AI to improve the quality of the code, and learn better the used ideas and concepts.



## Links
GitHub: https://github.com/turvasa/mobile-computing
