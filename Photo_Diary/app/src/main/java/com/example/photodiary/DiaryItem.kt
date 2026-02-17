package com.example.photodiary

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


/**
 * Entity representing a diary entry in the Room database
 *
 * @property id Auto-generated unique identifier for the diary entry.
 * @property imageName Filename of the associated image stored in app storage.
 * @property title Title of the diary entry.
 * @property description Optional description or notes for the diary entry.
 * @property temperature Optional temperature value recorded for the entry.
 * @property weather Optional weather description for the entry.
 * @property locationName Optional name of the location where the entry was created.
 */
@Entity
data class DiaryItem(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "imageName") val imageName: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "temperature") val temperature: Double?,
    @ColumnInfo(name = "weather") val weather: String?,
    @ColumnInfo(name = "locationName") val locationName: String?,

    //@ColumnInfo(name = "location") val location: String?
)