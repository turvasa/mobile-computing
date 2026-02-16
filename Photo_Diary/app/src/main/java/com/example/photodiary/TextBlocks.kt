package com.example.photodiary

interface TextBlocks {

    // Navigation
    val nav_home: String
    val nav_settings: String
    val nav_add: String

    // Titles
    val title_home: String
    val title_settings: String
    val title_add: String

    // Settings tab
    val settings_title_theme: String
    val settings_button_theme_dark: String
    val settings_button_theme_light: String
    val settings_title_language: String
    val settings_button_language: String
    val settings_button_time_confirm: String
    val settings_button_time_dismiss: String
    val settings_time_title: String
    val settings_time_change: String

    // Home tab
    val home_description: String
    val home_temperature: String
    val home_location: String

    // Add tab
    val add_create: String
    val add_create_new: String
    val add_info: String
    val add_info_title: String
    val add_info_description: String
    val add_file: String
    val add_file_choose: String
    val add_file_take_photo: String
    val add_weather: String

    // Errors
    val error_mandatory_field: String
    val error_exceeds_maximum_limit: String
    val error_must_be_integer: String
    val error_check: String
    val error_not_available: String

    // Others
    val loading: String
    val edit: String
    val details: String
    val update: String
    val delete: String
}