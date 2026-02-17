package com.example.photodiary


/**
 * Represents the weather data for a location
 *
 * @property main Contains the main weather metrics, e.g. temperature.
 * @property weather List of weather descriptions
 * @property name Name of the location
 */
data class Weather(
    val main: Main,
    val weather: List<WeatherDescription>,
    val name: String
) {

    companion object {
        /**
         * Formats temperature and weather into a readable string.
         *
         * @param temperature The temperature in °C.
         * @param weather The main weather description.
         * @param locationName The location name.
         * @return Formatted string like "Sunny || 25.0 °C || Oulu".
         */
        fun formatWeather(temperature: Double?, weather: String?, locationName: String?) : String {
            return "$weather || %.1f°C || $locationName".format(temperature)
        }
    }


    /**
     * Formats temperature and weather into a readable string.
     *
     * @return Formatted string like "Sunny || 25.0 °C || Oulu".
     */
    override fun toString() : String {
        val temperature: Double = getTemperature()
        val weather: String = getWeather()

        return "$weather || %.1f°C || $name".format(temperature)
    }


    /** @return The temperature in Celsius. */
    fun getTemperature(): Double {
        return main.temp
    }


    /** @return The main weather description, or "Unknown" if not available. */
    fun getWeather(): String {
        return weather.firstOrNull()?.main ?: "Unknown"
    }

}



/**
 * Holds main weather metrics such as temperature.
 *
 * @property temp Temperature in Celsius.
 */
data class Main(
    val temp: Double,
)



/**
 * Describes specific weather conditions.
 *
 * @property main Short descriptor of the weather.
 * @property description More detailed description.
 */
data class WeatherDescription(
    val main: String,
    val description: String
)