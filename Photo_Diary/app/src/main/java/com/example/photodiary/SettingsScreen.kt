package com.example.photodiary

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import androidx.room.util.TableInfo
import java.util.Calendar


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
    SetTabLayout(appColors) {
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

    val cardStyle = AppCardStyle(
        colors = CardDefaults.cardColors(
            containerColor = appColors.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .border(
                2.dp,
                appColors.primary2,
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

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 40.dp, bottom = 40.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){


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
                isDarkMode, appColors, appLanguage, isDefaultLocationUsed,
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
    appColors: AppColors, cardStyle: AppCardStyle,
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SetButton(isDarkMode, appColors, buttonText, settingFunction, buttonIcon)
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
    Row() {
        Text(
            text = switcherMessage,
            color = appColors.secondary3
        )
        Spacer(modifier = Modifier.padding(15.dp))

        // ON/OFF switcher
        Box(
            modifier = Modifier
                .padding(6.dp)
                .aspectRatio(1f)
                .combinedClickable(
                    onClick = { toggleOn }
                )
        ) {
            Image(
                painter = painterResource(
                    if (isOn) R.drawable.toggle_left else R.drawable.toggle_right
                ),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(32.dp)
                    .padding(start = 4.dp)
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
    cardStyle: AppCardStyle
) {

    // Card title
    val cardTitle = appLanguage.settings_title_theme

    // Button text
    val buttonText =
        if (isDarkMode) appLanguage.settings_button_theme_light
        else appLanguage.settings_button_theme_dark

    // Button icon
    val buttonIcon = painterResource(
        if (isDarkMode) R.drawable.icon_sun
        else R.drawable.icon_moon
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
    cardStyle: AppCardStyle
) {

    // Card title
    val cardTitle = appLanguage.settings_title_language

    // Button text
    val buttonText = appLanguage.settings_button_language

    // Button icon
    val buttonIcon = painterResource(
        if (isEnglish) R.drawable.flag_finnish
        else R.drawable.flag_english
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
    cardStyle: AppCardStyle,
) {

    // Card title
    val cardTitle = appLanguage.settings_time_title

    // Button text
    val buttonText = appLanguage.settings_time_change

    // Button icon
    val buttonIcon = painterResource(
        if (isDarkMode) R.drawable.icon_notification_light
        else R.drawable.icon_notification_light
    )

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
    changePreferenceTime: (Int, Int) -> Unit
    hideClock: () -> Unit, onClickEvent: () -> Unit,
    showTimePicker: Boolean,
    cardStyle: AppCardStyle,
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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Show edit time button
                if (!showTimePicker) {
                    SetOnOffSwitcher(
                        appColors, appLanguage.settings_time_toggle,
                        isNotificationON, toggleNotification
                    )
                    Spacer(modifier = Modifier.padding(10.dp))

                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        SetButton(isDarkMode, appColors, buttonText, onClickEvent, buttonIcon)
                        Spacer(modifier = Modifier.padding(10.dp))
                        Text(
                            text = "%02d:%02d".format(hour, minutes),
                            color = appColors.secondary3
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

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    )  {
        TimePicker(timePickerState)

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SetButton(
                isDarkMode, appColors, appLanguage.settings_button_time_confirm,
                { onConfirm(timePickerState.hour, timePickerState.minute) },
                painterResource(R.drawable.icon_confirm_time_light)
            )

            SetButton(
                isDarkMode, appColors, appLanguage.settings_button_time_dismiss,
                onDismiss, painterResource(R.drawable.icon_cancel_light)
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
    isDarkMode: Boolean,
    appColors: AppColors, appLanguage: TextBlocks,
    isDefaultLocation: Boolean, toggleDefaultLocationON: () -> Unit,
    changeDefaultLocation: (Float, Float) -> Unit,
    cardStyle: AppCardStyle
) {
    var isError by remember { mutableStateOf(false) }
    var toggleError: (Boolean) -> Unit =  { isError = it }
    val context = LocalContext.current

    // Card title
    val cardTitle = appLanguage.settings_title_theme

    // Button text
    val buttonText =
        if (isDarkMode) appLanguage.settings_button_theme_light
        else appLanguage.settings_button_theme_dark

    // Button icon
    val buttonIcon = painterResource(
        if (isDarkMode) R.drawable.icon_sun
        else R.drawable.icon_moon
    )

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

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 40.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                SetOnOffSwitcher(
                    appColors, appLanguage.settings_time_toggle,
                    isDefaultLocation, toggleDefaultLocationON
                )
                Spacer(modifier = Modifier.padding(10.dp))

                Row() {
                    SetButton(
                        isDarkMode, appColors, buttonText,
                        { getLocationSetOnClick(context, toggleError, changeDefaultLocation) },
                        buttonIcon
                    )

                    Spacer(modifier = Modifier.padding(15.dp))
                    Text(text = )
                }

                if (isError) {
                    Spacer(modifier = Modifier.padding(15.dp))
                    DisplayErrorMessage(appLanguage.error_getting_location)
                }

            }
        }
    }
}


fun getLocationSetOnClick(
    context: Context, toggleLocationError: (Boolean) -> Unit,
    changeDefaultLocation: (Float, Float) -> Unit
) {
    getCurrentLocation(
        context,
        changeDefaultLocation,
        toggleLocationError
    )
}
