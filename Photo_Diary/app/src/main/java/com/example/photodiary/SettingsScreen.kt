package com.example.photodiary

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


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
            // Formatting for setting cards
            val colors = CardDefaults.cardColors(
                containerColor = appColors.primary
            )
            val elevation = CardDefaults.cardElevation(
                defaultElevation = 20.dp
            )
            val modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .border(
                    2.dp,
                    appColors.primary2,
                    RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))

            SetDarkModeCard(isDarkMode, onToggleDarkMode, appColors, appLanguage, colors, elevation, modifier)

            Spacer(Modifier.height(20.dp))

            SetLanguageCard(isDarkMode, isEnglish, onToggleLanguage, appColors, appLanguage, colors, elevation, modifier)
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
    appColors: AppColors,
    colors: CardColors, elevation: CardElevation, modifier: Modifier
) {
    ElevatedCard(
        colors = colors,
        elevation = elevation,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TitleCard(appColors, cardTitle, 15.dp, 2.dp, false)
            SetButton(isDarkMode, appColors, buttonText, settingFunction, buttonIcon)
        }
    }
}




// -------------
// - Dark Mode -
// -------------


@Composable
fun SetDarkModeCard(isDarkMode: Boolean, onToggleDarkMode: () -> Unit, appColors: AppColors, appLanguage: TextBlocks, colors: CardColors, elevation: CardElevation, modifier: Modifier) {

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
        colors, elevation, modifier
    )
}




// ------------
// - Language -
// ------------


@Composable
fun SetLanguageCard(isDarkMode: Boolean, isEnglish: Boolean, onToggleLanguage: () -> Unit, appColors: AppColors, appLanguage: TextBlocks, colors: CardColors, elevation: CardElevation, modifier: Modifier) {

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
        colors, elevation, modifier
    )
}
