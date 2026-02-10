package com.example.photodiary

class TextFinnish : TextBlocks {
    // Navigation
    override val nav_home = "Koti"
    override val nav_settings = "Asetukset"
    override val nav_add = "Lisää"

    // Titles
    override val title_home = "Koti"
    override val title_settings = "Asetukset"
    override val title_add = "Lisää"

    // Settings tab
    override val settings_title_theme = "Teema"
    override val settings_button_theme_dark = "Tumma"
    override val settings_button_theme_light = "Vaalea"
    override val settings_title_language = "Kieli"
    override val settings_button_language = "English"

    // Home tab
    override val home_description = "Kuvaus"
    override val home_temperature = "Lämpötila"
    override val home_location = "Sijainti"

    // Add tab
    override val add_create = "Luo"
    override val add_create_new = "Luo uusi Merkintä"
    override val add_info = "Tiedot"
    override val add_info_title = "Nimi"
    override val add_info_description = "Kuvaus"
    override val add_file = "Kuva"
    override val add_file_choose = "Valitse tiedosto"
    override val add_file_take_photo = "Ota kuva"
    override val add_weather = "Sää"

    // Errors
    override val error_mandatory_field = "Pakollinen tieto!"
    override val error_exceeds_maximum_limit = "Ylittää suurimman sallitun merkki määrän: "
    override val error_must_be_integer = "Täytyy olla kokonaisluku!!"
    override val error_check = "Kelvottomia arvoja annettu! Tarkista kaikki kentät."
    override val error_not_available = "Ei saatavilla."

    // Others
    override val loading = "Ladataan..."
    override val edit = "Muokkaa"
    override val details = "Tiedot"
    override val update = "Päivitä"
    override val delete = "Poista"
}