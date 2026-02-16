package com.example.photodiary

import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.edit
import androidx.room.util.TableInfo
import java.util.Calendar


@Composable
fun SettingsCard(
    isDarkMode: Boolean,
    isEnglish: Boolean,
    onToggleDarkMode: () -> Unit,
    onToggleLanguage: () -> Unit,
    appColors: AppColors,
    appLanguage: TextBlocks
) {
    SetTabLayout(appColors) {
        SetBody(
            isDarkMode, isEnglish,
            onToggleDarkMode, onToggleLanguage,
            appColors, appLanguage
        )
    }

}


@Composable
fun SetBody(
    isDarkMode: Boolean, isEnglish: Boolean,
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
            SetDarkModeCard(isDarkMode, onToggleDarkMode, appColors, appLanguage, cardStyle)

            // Language
            Spacer(Modifier.height(20.dp))
            SetLanguageCard(isDarkMode, isEnglish, onToggleLanguage, appColors, appLanguage, cardStyle)

            // Notification time
            if (ActivityCompat.checkSelfPermission(
                    LocalContext.current, Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Spacer(Modifier.height(20.dp))
                SetNotificationTimeCard(isDarkMode, appColors, appLanguage, cardStyle)
            }
        }
    }
}




// ------------------
// - General Layout -
// ------------------


@Composable
fun SetSettingCard(
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




// -------------
// - Dark Mode -
// -------------


@Composable
fun SetDarkModeCard(
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


@Composable
fun SetLanguageCard(
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




// ------------
// - Language -
// ------------


@Composable
fun SetNotificationTimeCard(
    isDarkMode: Boolean,
    appColors: AppColors, appLanguage: TextBlocks,
    cardStyle: AppCardStyle,
) {
    val context = LocalContext.current

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
    val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    var hour by remember { mutableIntStateOf(preferences.getInt("notification_hour", 20)) }
    var minutes by remember { mutableIntStateOf(preferences.getInt("notification_minutes", 0)) }
    var showTimePicker by remember { mutableStateOf(false) }
    val onClickEvent = { showTimePicker = true }


    SetSettingTimeCard(
        isDarkMode, appColors, appLanguage,
        cardTitle, buttonText, buttonIcon,
        preferences, hour, minutes,
        { hour = it }, { minutes = it },
        { showTimePicker = false },
        onClickEvent, showTimePicker, cardStyle
    )
}


@Composable
fun SetSettingTimeCard(
    isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks,
    cardTitle: String, buttonText: String, buttonIcon: Painter,
    preferences: SharedPreferences, hour: Int, minutes: Int,
    toggleHour: (Int) -> Unit, toggleMinutes: (Int) -> Unit,
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
                            toggleHour(newHour)
                            toggleMinutes(newMinutes)
                            saveNotificationTime(preferences, newHour, newMinutes)
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetDialTimeInput(
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


fun saveNotificationTime(preferences: SharedPreferences, hour: Int, minutes: Int) {
    preferences.edit {
         putInt("notification_hour", hour)
        .putInt("notification_minutes", minutes)
    }
}
