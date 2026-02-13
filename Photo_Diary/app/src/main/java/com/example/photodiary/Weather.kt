package com.example.photodiary




data class Weather(
    val main: Main,
    val weather: List<WeatherDescription>,
    val name: String
) {

    companion object {
        /**
         * Formats temperature and weather into a readable string.
         *
         * @param temperature The temperature in °C
         * @param weather The main weather description
         * @return Formatted string like "Sunny - 25.0 °C"
         */
        fun formatWeather(temperature: Double?, weather: String?, locationName: String?) : String {
            return "$weather || %.1f°C || $locationName".format(temperature)
        }
    }


    override fun toString() : String {
        val temperature: Double = getTemperature()
        val weather: String = getWeather()

        return "$weather || %.1f°C || $name".format(temperature)
    }


    fun getTemperature(): Double {
        return main.temp
    }

    fun getWeather(): String {
        return weather.firstOrNull()?.main ?: "Unknown"
    }

}




data class Main(
    val temp: Double,
)




data class WeatherDescription(
    val main: String,
    val description: String
)