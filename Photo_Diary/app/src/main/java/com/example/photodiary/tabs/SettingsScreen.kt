package com.example.photodiary.tabs

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.example.photodiary.TitleCard
import com.example.photodiary.colors.AppColors
import com.example.photodiary.language.TextBlocks
import com.example.photodiary.location.areLocationPermissionsGranted
import com.example.photodiary.location.getCurrentLocation
import com.example.photodiary.location.getLocationName
import com.example.photodiary.location.rememberLocationPermissionLauncher
import java.util.Locale


/**
 * Composable card for SETTINGS location screen.
 * Used for changing theme, language, notification timing and more.
 *
 * @param isDarkMode Current dark mode state.
 * @param isEnglish Current language state (true = English, false = Finnish).
 * @param onToggleDarkMode Callback to toggle dark mode.
 * @param onToggleLanguage Callback to toggle language.
 * @param appColors Color palette to style the UI.
 * @param appLanguage Text strings for the current language.
 */
@Composable
fun SettingsCard(
    isDarkMode: Boolean, isEnglish: Boolean,
    isNotificationON: Boolean, toggleNotification: () -> Unit,
    hour: Int, minutes: Int, changePreferenceTime: (Int, Int) -> Unit,
    latitude: Float, longitude: Float, changeDefaultLocation: (Float, Float) -> Unit,
    isDefaultLocationUsed: Boolean, toggleDefaultLocationON: () -> Unit,
    onToggleDarkMode: () -> Unit, onToggleLanguage: () -> Unit,
    appColors: AppColors, appLanguage: TextBlocks
) {
    _root_ide_package_.com.example.photodiary.SetTabLayout(appColors) {
        SetBody(
            isDarkMode, isEnglish,
            isNotificationON, toggleNotification,
            hour, minutes, changePreferenceTime,
            latitude, longitude, changeDefaultLocation,
            isDefaultLocationUsed, toggleDefaultLocationON,
            onToggleDarkMode, onToggleLanguage,
            appColors, appLanguage
        )
    }

}


/**
 * Sets up the body for the SETTINGS destination screen.
 * Displays button oriented setting cards for the settings sections.
 *
 * @param isDarkMode Current dark mode state.
 * @param isEnglish Current language state.
 * @param onToggleDarkMode Callback to toggle dark mode.
 * @param onToggleLanguage Callback to toggle language.
 * @param appColors Color palette for UI elements.
 * @param appLanguage Text strings for the current language.
 */
@Composable
private fun SetBody(
    isDarkMode: Boolean, isEnglish: Boolean,
    isNotificationON: Boolean, toggleNotification: () -> Unit,
    hour: Int, minutes: Int, changePreferenceTime: (Int, Int) -> Unit,
    latitude: Float, longitude: Float, changeDefaultLocation: (Float, Float) -> Unit,
    isDefaultLocationUsed: Boolean, toggleDefaultLocationON: () -> Unit,
    onToggleDarkMode: () -> Unit, onToggleLanguage: () -> Unit,
    appColors: AppColors, appLanguage: TextBlocks
) {

    val cardStyle = _root_ide_package_.com.example.photodiary.AppCardStyle(
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                3.dp,
                appColors.cardBorder,
                RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
    )



    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.TopCenter
    ) {

        TitleCard(appColors, appLanguage.title_settings, 6.dp, 0.dp, true)

        _root_ide_package_.com.example.photodiary.SetDefaultColumn(
            paddingValues = PaddingValues(top = 40.dp, bottom = 40.dp, start = 20.dp, end = 20.dp)
        ) {
            // Dark mode
            SetDarkModeCard(
                isDarkMode, onToggleDarkMode,
                appColors, appLanguage, cardStyle
            )

            // Language
            Spacer(Modifier.height(20.dp))
            SetLanguageCard(
                isDarkMode, isEnglish, onToggleLanguage,
                appColors, appLanguage, cardStyle
            )

            // Notification time
            if (ActivityCompat.checkSelfPermission(
                    LocalContext.current, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Spacer(Modifier.height(20.dp))
                SetNotificationTimeCard(
                    isDarkMode, isNotificationON, toggleNotification,
                    hour, minutes, changePreferenceTime,
                    appColors, appLanguage, cardStyle
                )
            }

            // Location
            Spacer(Modifier.height(20.dp))
            SetLocationCard(
                isDarkMode, isEnglish, appColors, appLanguage,
                latitude, longitude, isDefaultLocationUsed,
                toggleDefaultLocationON, changeDefaultLocation,
                cardStyle
            )
        }
    }
}




// ------------------
// - General Layout -
// ------------------


/**
 * General layout for a single setting section with title and the setting button.
 *
 * @param isDarkMode Current dark mode state.
 * @param cardTitle Title displayed on the card.
 * @param buttonText Text displayed on the card's button.
 * @param buttonIcon Icon displayed on the card's button.
 * @param settingFunction Callback invoked when button is pressed.
 * @param appColors Color palette for the UI.
 * @param cardStyle Style to apply to the card.
 */
@Composable
private fun SetSettingCard(
    isDarkMode: Boolean, cardTitle: String,
    buttonText: String, buttonIcon: Painter,
    settingFunction: () -> Unit,
    appColors: AppColors, cardStyle: com.example.photodiary.AppCardStyle,
) {
    ElevatedCard(
        colors = cardStyle.colors,
        elevation = cardStyle.elevation,
        modifier = cardStyle.modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            TitleCard(appColors, cardTitle, 15.dp, 2.dp, false)

            _root_ide_package_.com.example.photodiary.SetDefaultColumn(
                PaddingValues(
                    top = 40.dp,
                    bottom = 40.dp
                )
            ) {
                _root_ide_package_.com.example.photodiary.SetButton(
                    isDarkMode,
                    appColors,
                    buttonText,
                    settingFunction,
                    buttonIcon
                )
            }
        }
    }
}


/**
 * Sets up ON/OFF switcher.
 */
@Composable
fun SetOnOffSwitcher(
    appColors: AppColors, switcherMessage: String,
    isOn: Boolean, toggleOn: () -> Unit
) {
    _root_ide_package_.com.example.photodiary.SetDefaultRow(PaddingValues(0.dp)) {
        Text(
            text = switcherMessage,
            color = appColors.mainText
        )

        Spacer(modifier = Modifier.padding(2.dp))

        // ON/OFF switcher
        Box(
            modifier = Modifier
                .combinedClickable(
                    onClick = { toggleOn() }
                )
                .wrapContentHeight()
                .clip(RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            _root_ide_package_.com.example.photodiary.DisplayIcon(
                painterResource(
                    if (isOn) _root_ide_package_.com.example.photodiary.R.drawable.toggle_right
                    else _root_ide_package_.com.example.photodiary.R.drawable.toggle_left
                ),
                48.dp
            )
        }
    }
}




// -------------
// - Dark Mode -
// -------------


/**
 * Displays the Dark Mode setting section
 *
 * @param isDarkMode Current dark mode state.
 * @param onToggleDarkMode Callback to toggle dark mode.
 * @param appColors Color palette for UI.
 * @param appLanguage Language strings.
 * @param cardStyle Card style for the dark mode section.
 */
@Composable
private fun SetDarkModeCard(
    isDarkMode: Boolean, onToggleDarkMode: () -> Unit,
    appColors: AppColors, appLanguage: TextBlocks,
    cardStyle: com.example.photodiary.AppCardStyle
) {

    // Card title
    val cardTitle = appLanguage.settings_title_theme

    // Button text
    val buttonText =
        if (isDarkMode) appLanguage.settings_button_theme_light
        else appLanguage.settings_button_theme_dark

    // Button icon
    val buttonIcon = painterResource(
        if (isDarkMode) _root_ide_package_.com.example.photodiary.R.drawable.icon_sun
        else _root_ide_package_.com.example.photodiary.R.drawable.icon_moon
    )

    SetSettingCard(
        isDarkMode, cardTitle,
        buttonText, buttonIcon,
        onToggleDarkMode,
        appColors,
        cardStyle
    )
}




// ------------
// - Language -
// ------------


/**
 * Displays the Language setting section
 *
 * @param isDarkMode Current dark mode state.
 * @param isEnglish Current language state
 * @param onToggleLanguage Callback to toggle dark mode.
 * @param appColors Color palette for UI.
 * @param appLanguage Language strings.
 * @param cardStyle Card style for the dark mode section.
 */
@Composable
private fun SetLanguageCard(
    isDarkMode: Boolean, isEnglish: Boolean,
    onToggleLanguage: () -> Unit, appColors: AppColors, appLanguage: TextBlocks,
    cardStyle: com.example.photodiary.AppCardStyle
) {

    // Card title
    val cardTitle = appLanguage.settings_title_language

    // Button text
    val buttonText = appLanguage.settings_button_language

    // Button icon
    val buttonIcon = painterResource(
        if (isEnglish) _root_ide_package_.com.example.photodiary.R.drawable.flag_finnish
        else _root_ide_package_.com.example.photodiary.R.drawable.flag_english
    )

    SetSettingCard(
        isDarkMode, cardTitle,
        buttonText, buttonIcon,
        onToggleLanguage,
        appColors,
        cardStyle
    )
}




// ----------------
// - Notification -
// ----------------


/**
 * Displays the Notification Timing setting section
 *
 * @param isDarkMode Current dark mode state.
 * @param preferences SharedPreferences storing the settings.
 * @param appColors Color palette for UI.
 * @param appLanguage Language strings.
 * @param cardStyle Card style for the dark mode section.
 */
@Composable
private fun SetNotificationTimeCard(
    isDarkMode: Boolean,
    isNotificationON: Boolean, toggleNotification: () -> Unit,
    hour: Int, minutes: Int, changePreferenceTime: (Int, Int) -> Unit,
    appColors: AppColors, appLanguage: TextBlocks,
    cardStyle: com.example.photodiary.AppCardStyle,
) {

    // Card title
    val cardTitle = appLanguage.settings_time_title

    // Button text
    val buttonText = appLanguage.settings_time_change

    // Button icon
    val buttonIcon = painterResource(_root_ide_package_.com.example.photodiary.R.drawable.icon_notification_clock)

    // Button on click event

    var showTimePicker by remember { mutableStateOf(false) }
    val onClickEvent = { showTimePicker = true }


    SetSettingTimeCard(
        isDarkMode, appColors, appLanguage,
        cardTitle, buttonText, buttonIcon,
        hour, minutes,
        isNotificationON, toggleNotification, changePreferenceTime,
        { showTimePicker = false },
        onClickEvent, showTimePicker, cardStyle
    )
}


/**
 * Layout for the notification time card including the time display and the button.
 *
 * @param isDarkMode Current dark mode state.
 * @param appColors Color palette for UI.
 * @param appLanguage Language strings.
 * @param cardTitle Title for the card.
 * @param buttonText Text for the edit button.
 * @param buttonIcon Icon for the edit button.
 * @param preferences SharedPreferences storing the settings.
 * @param hour Current hour for the notification.
 * @param minutes Current minutes for the notification.
 * @param toggleHour Callback to update hour state.
 * @param toggleMinutes Callback to update minutes state.
 * @param hideClock Callback to hide the time picker.
 * @param onClickEvent Callback to show the time picker.
 * @param showTimePicker Whether to show the time picker.
 * @param cardStyle Style to apply to the card.
 */
@Composable
private fun SetSettingTimeCard(
    isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks,
    cardTitle: String, buttonText: String, buttonIcon: Painter,
    hour: Int, minutes: Int,
    isNotificationON: Boolean, toggleNotification: () -> Unit,
    changePreferenceTime: (Int, Int) -> Unit,
    hideClock: () -> Unit, onClickEvent: () -> Unit,
    showTimePicker: Boolean,
    cardStyle: com.example.photodiary.AppCardStyle,
) {
    ElevatedCard(
        colors = cardStyle.colors,
        elevation = cardStyle.elevation,
        modifier = cardStyle.modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TitleCard(appColors, cardTitle, 15.dp, 2.dp, false)

            _root_ide_package_.com.example.photodiary.SetDefaultColumn(
                PaddingValues(
                    top = 40.dp,
                    bottom = 40.dp
                )
            ) {
                // Show edit time button
                if (!showTimePicker) {
                    SetOnOffSwitcher(
                        appColors, appLanguage.settings_time_toggle,
                        isNotificationON, toggleNotification
                    )
                    Spacer(modifier = Modifier.padding(10.dp))

                    _root_ide_package_.com.example.photodiary.SetDefaultRow(PaddingValues(0.dp)) {
                        _root_ide_package_.com.example.photodiary.SetButton(
                            isDarkMode,
                            appColors,
                            buttonText,
                            onClickEvent,
                            buttonIcon
                        )
                        Spacer(modifier = Modifier.padding(10.dp))
                        Text(
                            text = "%02d:%02d".format(hour, minutes),
                            color = appColors.mainText
                        )
                    }
                }

                // Show the clock
                else {
                    SetDialTimeInput(
                        isDarkMode, appColors, appLanguage,
                        hour, minutes,
                        onConfirm = { newHour, newMinutes ->
                            changePreferenceTime(newHour, newMinutes)
                            hideClock()
                        },
                        onDismiss = {
                            hideClock()
                        }
                    )
                }
            }
        }
    }
}


/**
 * Displays the time picker clock fro editing the notification timing.
 *
 * @param isDarkMode Current dark mode state.
 * @param appColors Color palette for UI.
 * @param appLanguage Language strings.
 * @param hour Current hour to display.
 * @param minutes Current minutes to display.
 * @param onConfirm Callback invoked with the new hour and minutes.
 * @param onDismiss Callback invoked when the user cancels the picker.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SetDialTimeInput(
    isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks,
    hour: Int, minutes: Int,
    onConfirm: (Int, Int) -> Unit, onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minutes,
        is24Hour = true
    )

    _root_ide_package_.com.example.photodiary.SetDefaultColumn(paddingValues = PaddingValues(0.dp)) {
        TimePicker(timePickerState)

        _root_ide_package_.com.example.photodiary.SetDefaultRow(paddingValues = PaddingValues(0.dp)) {
            _root_ide_package_.com.example.photodiary.SetButton(
                isDarkMode, appColors, appLanguage.settings_button_time_confirm,
                { onConfirm(timePickerState.hour, timePickerState.minute) },
                painterResource(_root_ide_package_.com.example.photodiary.R.drawable.icon_confirm_time)
            )

            _root_ide_package_.com.example.photodiary.SetButton(
                isDarkMode,
                appColors,
                appLanguage.settings_button_time_dismiss,
                onDismiss,
                painterResource(_root_ide_package_.com.example.photodiary.R.drawable.icon_cancel)
            )
        }
    }
}




// ------------
// - Location -
// ------------


/**
 * Displays the Location setting section
 *
 * @param isDarkMode Current dark mode state.
 * @param appColors Color palette for UI.
 * @param appLanguage Language strings.
 * @param cardStyle Card style for the dark mode section.
 */
@Composable
private fun SetLocationCard(
    isDarkMode: Boolean, isEnglish: Boolean,
    appColors: AppColors, appLanguage: TextBlocks,
    latitude: Float, longitude: Float,
    isDefaultLocation: Boolean, toggleDefaultLocationON: () -> Unit,
    changeDefaultLocation: (Float, Float) -> Unit,
    cardStyle: com.example.photodiary.AppCardStyle
) {
    var isError by remember { mutableStateOf(false) }
    val toggleError: (Boolean) -> Unit =  { isError = it }
    val context = LocalContext.current

    // Card title
    val cardTitle = appLanguage.settings_title_location

    // Button text
    val buttonText = appLanguage.settings_location_set_default

    // Button icon
    val buttonIcon = painterResource(_root_ide_package_.com.example.photodiary.R.drawable.icon_location)

    // Location permissions
    val launchPermissionRequest =
        rememberLocationPermissionLauncher(
            {
                getCurrentLocation(
                    context,
                    changeDefaultLocation,
                    toggleError
                )
            },
            {
                toggleError(true)
            }
        )

    // Get location names
    var cityName by remember { mutableStateOf<String?>(null) }
    var countryName by remember { mutableStateOf<String?>(null) }
    val locale = if (isEnglish) Locale.ENGLISH else Locale.forLanguageTag("fi")
    LaunchedEffect(latitude, longitude, locale) { // React to locale (isEnglish) changes as well
        // This makes the default location update happen visually
        cityName = null
        countryName = null

        getLocationName(
            latitude,
            longitude,
            context,
            { cityName = it },
            { countryName = it },
            locale
        )
    }

    ElevatedCard(
        colors = cardStyle.colors,
        elevation = cardStyle.elevation,
        modifier = cardStyle.modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            TitleCard(appColors, cardTitle, 15.dp, 2.dp, false)

            _root_ide_package_.com.example.photodiary.SetDefaultColumn(
                PaddingValues(
                    top = 40.dp,
                    bottom = 40.dp
                )
            ) {
                SetLocationCardBody(
                    isDarkMode, appColors, appLanguage,
                    isDefaultLocation, toggleDefaultLocationON,
                    changeDefaultLocation, cityName, countryName,
                    isError, toggleError, buttonText, buttonIcon,
                    launchPermissionRequest, context
                )
            }
        }
    }
}


/**
 * Sets the card body layout for the location.
 *
 * @param isDarkMode Current dark mode state.
 * @param appColors Color palette for UI styling.
 * @param appLanguage Localized text provider.
 * @param isDefaultLocation Indicates whether default location usage is enabled.
 * @param toggleDefaultLocationON Callback to toggle default location setting.
 * @param changeDefaultLocation Callback invoked when location coordinates are updated.
 * @param cityName Name of the detected city (nullable).
 * @param countryName Name of the detected country (nullable).
 * @param isError Indicates whether a location retrieval error occurred.
 * @param toggleError Callback to update error state.
 * @param buttonText Text displayed on the location action button.
 * @param buttonIcon Icon displayed on the location action button.
 * @param launchPermissionRequest Function to launch runtime location permission request.
 * @param context Android context used for location services.
 */
@Composable
private fun SetLocationCardBody(
    isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks,
    isDefaultLocation: Boolean, toggleDefaultLocationON: () -> Unit,
    changeDefaultLocation: (Float, Float) -> Unit,
    cityName: String?, countryName: String?,
    isError: Boolean, toggleError: (Boolean) -> Unit,
    buttonText: String, buttonIcon: Painter,
    launchPermissionRequest: () -> Unit,
    context: Context
) {
    SetOnOffSwitcher(
        appColors, appLanguage.settings_location_toggle,
        isDefaultLocation, toggleDefaultLocationON
    )
    Spacer(modifier = Modifier.padding(10.dp))

    _root_ide_package_.com.example.photodiary.SetDefaultRow(PaddingValues(0.dp)) {
        _root_ide_package_.com.example.photodiary.SetButton(
            isDarkMode, appColors, buttonText,
            {
                if (areLocationPermissionsGranted(context)) {
                    getCurrentLocation(
                        context,
                        changeDefaultLocation,
                        toggleError
                    )
                } else {
                    launchPermissionRequest()
                }
            },
            buttonIcon
        )

        Spacer(modifier = Modifier.padding(3.dp))
        Text(
            text = listOfNotNull(cityName, countryName).joinToString(", "),
            color = appColors.mainText
        )

        _root_ide_package_.com.example.photodiary.DisplayIcon(
            painterResource(_root_ide_package_.com.example.photodiary.R.drawable.icon_pin),
            25.dp
        )
    }

    // Error message
    if (isError) {
        Spacer(modifier = Modifier.padding(5.dp))
        DisplayErrorMessage(appLanguage.error_getting_location)
    }
}


