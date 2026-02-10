package com.example.photodiary

import android.accessibilityservice.GestureDescription
import android.graphics.BitmapFactory
import android.health.connect.datatypes.units.Temperature
import android.net.Uri
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.File

@Entity
data class DiaryItem(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "imageName") val imageName: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "temperature") val temperature: Float?,
    @ColumnInfo(name = "weather") val weather: String?,

    //@ColumnInfo(name = "location") val location: String?
)