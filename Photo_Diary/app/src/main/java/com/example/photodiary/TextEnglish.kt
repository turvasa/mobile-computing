package com.example.photodiary

/**
 * English implementation of [TextBlocks], providing all textual content
 * for the Photo Diary app in English.
 */
class TextEnglish : TextBlocks {



    // --------------
    // - Navigation -
    // --------------
    override val nav_home = "Home"
    override val nav_settings = "Settings"
    override val nav_add = "Add"



    // ----------
    // - Titles -
    // ----------
    override val title_home = "Home"
    override val title_settings = "Settings"
    override val title_add = "Add"



    // ----------------
    // - Settings tab -
    // ----------------
    override val settings_title_theme = "Theme"
    override val settings_button_theme_dark = "Dark Mode"
    override val settings_button_theme_light = "Light Mode"
    override val settings_title_language = "Language"
    override val settings_button_language = "Suomi"
    override val settings_button_time_confirm = "Confirm"
    override val settings_button_time_dismiss = "Dismiss"
    override val settings_time_title = "Notifications"
    override val settings_time_change = "Timing"
    override val settings_time_toggle = "Allow notifications:"
    override val setting_title_location = "Location"
    override val setting_location_set_default = "Define default location"
    override val settings_location_toggle = "Use ONLY default location:"



    // ------------
    // - Home tab -
    // ------------
    override val home_description = "Description"
    override val home_temperature = "Temperature"
    override val home_location = "Location"



    // -----------
    // - Add tab -
    // -----------
    override val add_create = "Create"
    override val add_create_new = "Create new Entry"
    override val add_info = "Information"
    override val add_info_title = "Title"
    override val add_info_description = "Description"
    override val add_file = "Image"
    override val add_file_choose = "Choose file"
    override val add_file_take_photo = "Take photo"
    override val add_weather = "Weather"



    // ----------
    // - Errors -
    // ----------
    override val error_mandatory_field = "Mandatory field!"
    override val error_exceeds_maximum_limit = "Exceeds the maximum char limit: "
    override val error_must_be_integer = "Value must be integer!"
    override val error_check = "Invalid values given! Check all fields."
    override val error_not_available = "Not available."
    override val error_getting_location = "Unable to get location. Try again."



    // -----------------
    // - Miscellaneous -
    // -----------------
    override val loading = "Loading..."
    override val edit = "Edit"
    override val details = "Details"
    override val update = "Update"
    override val delete = "Delete"
}
