package com.example.photodiary

data class Weather(
    val temperature: Float?,
    val weather: String?
) {

    override fun toString() : String {
        return String.format("$weather - %.1F °C", temperature)
    }

    fun isEmpty() : Boolean {
        return (temperature == null || weather == null)
    }
}