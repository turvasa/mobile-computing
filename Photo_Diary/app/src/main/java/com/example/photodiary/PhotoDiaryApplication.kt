package com.example.photodiary

import android.app.Application
import androidx.room.Room

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