package com.example.photodiary

/**
 * Interface defining all textual content used throughout the Photo Diary app.
 *
 * Implementations of this interface allow the app to support multiple languages
 * by providing localized strings for navigation, titles, settings, home/add tabs,
 * error messages, and other UI elements.
 */
interface TextBlocks {



    // --------------
    // - Navigation -
    // --------------
    /** Label for the Home navigation tab */
    val nav_home: String
    /** Label for the Settings navigation tab */
    val nav_settings: String
    /** Label for the Add entry navigation tab */
    val nav_add: String



    // ----------
    // - Titles -
    // ----------
    /** Title displayed on the Home screen */
    val title_home: String
    /** Title displayed on the Settings screen */
    val title_settings: String
    /** Title displayed on the Add screen */
    val title_add: String



    // ----------------
    // - Settings tab -
    // ----------------
    /** Title for theme/dark mode section */
    val settings_title_theme: String
    /** Button text to enable dark mode */
    val settings_button_theme_dark: String
    /** Button text to enable light mode */
    val settings_button_theme_light: String
    /** Title for language section */
    val settings_title_language: String
    /** Button text to change language */
    val settings_button_language: String
    /** Button text to confirm time selection */
    val settings_button_time_confirm: String
    /** Button text to dismiss time selection */
    val settings_button_time_dismiss: String
    /** Title for notification time section */
    val settings_time_title: String
    /** Button text to change notification time */
    val settings_time_change: String
    /** Toggle notification ON/OFF description */
    val settings_time_toggle: String
    /** Title for location section */
    val setting_title_location: String
    /** Description of defining default location */
    val setting_location_set_default: String
    /** Toggle default location ON/OFF description */
    val settings_location_toggle: String



    // ------------
    // - Home tab -
    // ------------
    /** Description shown on Home screen for entries */
    val home_description: String
    /** Label for temperature info on Home screen */
    val home_temperature: String
    /** Label for location info on Home screen */
    val home_location: String



    // -----------
    // - Add tab -
    // -----------
    /** Button text to create a new diary entry */
    val add_create: String
    /** Label for creating a new diary entry */
    val add_create_new: String
    /** Title for additional information section */
    val add_info: String
    /** Label for entry title field */
    val add_info_title: String
    /** Label for entry description field */
    val add_info_description: String
    /** Label for file section */
    val add_file: String
    /** Button text to choose a file from storage */
    val add_file_choose: String
    /** Button text to take a new photo */
    val add_file_take_photo: String
    /** Label for weather information section */
    val add_weather: String



    // ----------
    // - Errors -
    // ----------
    /** Error shown when a mandatory field is empty */
    val error_mandatory_field: String
    /** Error shown when input exceeds the maximum allowed limit */
    val error_exceeds_maximum_limit: String
    /** Error shown when input must be an integer */
    val error_must_be_integer: String
    /** General check error message */
    val error_check: String
    /** Error shown when a feature or data is not available */
    val error_not_available: String

    /** Unable to get location error */
    val error_getting_location: String



    // -----------------
    // - Miscellaneous -
    // -----------------
    /** Text shown while loading data */
    val loading: String
    /** Text for the edit button or screen */
    val edit: String
    /** Text for details view */
    val details: String
    /** Text for update button */
    val update: String
    /** Text for delete button */
    val delete: String
}
