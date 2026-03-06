package com.example.photodiary

import android.app.Application
import androidx.room.Room
import com.example.photodiary.database.AppDatabase


/**
 * Application class for PhotoDiary app.
 * Responsible for initializing global application-level recourses.
 */
class PhotoDiaryApplication : Application() {

    lateinit var db: AppDatabase

    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "diary-db",
        )
            .enableMultiInstanceInvalidation()
            .build()
    }

}