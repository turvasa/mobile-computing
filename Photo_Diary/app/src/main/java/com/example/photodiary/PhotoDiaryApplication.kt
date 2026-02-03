package com.example.photodiary

import android.app.Application
import androidx.room.Room

class PhotoDiaryApplication : Application() {

    val db = Room.databaseBuilder(
        applicationContext,
        AppDatabase::class.java, "diary-db",
    )
        .enableMultiInstanceInvalidation()
        .build()

}