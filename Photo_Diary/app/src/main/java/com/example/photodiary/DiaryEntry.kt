package com.example.photodiary

import android.graphics.BitmapFactory
import android.health.connect.datatypes.units.Temperature
import android.provider.CallLog
import java.io.File

class DiaryEntry {


    private val errorMessage = "DiaryEntry - "


    var imagePath: String = "null"
        private set(path: String) {

            // Check is empty
            if (path.isEmpty()) {
                throw NullPointerException(errorMessage + "Image path must be given.")
            }

            // Check is valid file
            val file = File(path)
            if (!file.exists()) {
                throw IllegalArgumentException(errorMessage + "Image path don't lead to anything.")
            }

            // Check is valid image file (by GPT)
            val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            BitmapFactory.decodeFile(file.absolutePath, options)
            if (options.outWidth <= 0 || options.outHeight <= 0) {
                throw IllegalArgumentException(errorMessage + "Image path don't lead to image file.")
            }

            field = path
        }


    var title: String = "No Title"
        private set(title) {

            // Check is empty
            if (title.isEmpty()) {
                throw NullPointerException(errorMessage + "Title must be given")
            }

            field = title
        }

/*
    var description: String = "No Description"
        private set

    var temperature: Temperature = Temperature.fromCelsius(0.0)
        private set(temperature) {
            field = Temperature.fromCelsius(0.0)
        }

    var location: Location = "No Location"
        private set
*/



    constructor(
        path: String,
        title: String,

        /*
        description: String,
        temperature: Temperature,
        location: Location
         */
    )

}