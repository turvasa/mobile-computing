# Photo Diary

PhotoDiary is an Android application built with Kotlin (Jetpack Compose). The app
allows users to create personal diary entries with images, location data and optional
weather information.


## Features
* Capture images using the camera app of the device
* Select images from device gallery
* Add title and description to the diary entry
* Store location data (latitude & longitude)
* Save weather information (temperature & weather icon as ID)
* View entries in a grid layout
* Detailed view for each entry, and edit & delete options
* Optional daily notification reminder
* Light/Dark theme support
* Multi-language support (English & Finnish)


## Architecture & Technologies
* Main language: Kotlin
* Jetpack Compose - UI development
* Room Database - Local data storage
* Coil - Weather icon loading from the weather API
* Shared preferences - Permanent setting storage
* Retrofit2 - API calls


## How to Run
1. Clone the repository
```terminal
git clone https://github.com/turvasa/mobile-computing/PhotoDiary.git
```
2. Open with [Android Studio]("https://developer.android.com/studio")
3. Sync Gradle
4. Run on emulator or physical Android device using USB cable (NOTE: Phone's developer mode must be ON)


## Future Improvements
* Entry search with filtering and ordering
* Backup and export functionalities
