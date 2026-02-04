package com.example.photodiary

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun SettingsCard(
    isDarkMode: Boolean,
    isEnglish: Boolean,
    onToggleDarkMode: () -> Unit,
    onToggleLanguage: () -> Unit,
    appColors: AppColors,
    appLanguage: TextBlocks,
    diaryItemDAO: DiaryItemDAO,
    viewModel: DatabaseMethods
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp, bottom = 30.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = appColors.primary.copy(alpha = 0.8f)
            ),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight()
                .border(
                    3.dp,
                    appColors.primary2,
                    RoundedCornerShape(10.dp)
                )
                .clip(RoundedCornerShape(10.dp))
        ) {
            SetBody(isDarkMode, isEnglish, onToggleDarkMode, onToggleLanguage, appColors, appLanguage)
        }
    }

}


@Composable
fun SetBody(isDarkMode: Boolean, isEnglish: Boolean, onToggleDarkMode: () -> Unit, onToggleLanguage: () -> Unit, appColors: AppColors, appLanguage: TextBlocks) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        TitleCard(appColors, appLanguage.title_settings, 6.dp, 0.dp, true)

        Column(
            modifier = Modifier.fillMaxSize(0.8f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
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
            SetLanguageCard(isDarkMode, isEnglish, onToggleLanguage, appColors, appLanguage, colors, elevation, modifier)
        }
    }
}




// -------------
// - Dark Mode -
// -------------


@Composable
fun SetDarkModeCard(isDarkMode: Boolean, onToggleDarkMode: () -> Unit, appColors: AppColors, appLanguage: TextBlocks, colors: CardColors, elevation: CardElevation, modifier: Modifier) {
    ElevatedCard(
        colors = colors,
        elevation = elevation,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TitleCard(appColors, appLanguage.settings_title_theme, 15.dp, 2.dp, false)
            SetDarkModeButton(isDarkMode, onToggleDarkMode, appColors, appLanguage)
        }
    }
}


@Composable
fun SetDarkModeButton(isDarkMode: Boolean, onToggleDarkMode: () -> Unit, appColors: AppColors, appLanguage: TextBlocks) {

    // Button text
    val text =
        if (isDarkMode) appLanguage.settings_button_theme_light
        else appLanguage.settings_button_theme_dark

    // Button icon
    val icon = painterResource(
        if (isDarkMode) R.drawable.icon_sun
        else R.drawable.icon_moon
    )

    SetButton(isDarkMode, appColors, text, onToggleDarkMode, icon)
}




// ------------
// - Language -
// ------------


@Composable
fun SetLanguageCard(isDarkMode: Boolean, isEnglish: Boolean, onToggleLanguage: () -> Unit, appColors: AppColors, appLanguage: TextBlocks, colors: CardColors, elevation: CardElevation, modifier: Modifier) {
    ElevatedCard(
        colors = colors,
        elevation = elevation,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TitleCard(appColors, appLanguage.settings_title_language, 15.dp, 2.dp, false)
            SetLanguageButton(isDarkMode, isEnglish, onToggleLanguage, appColors, appLanguage)
        }
    }


}


@Composable
fun SetLanguageButton(isDarkMode: Boolean, isEnglish: Boolean, onToggleLanguage: () -> Unit, appColors: AppColors, appLanguage: TextBlocks) {

    // Button text
    val text = appLanguage.settings_button_language

    // Button icon
    val icon = painterResource(
        if (isEnglish) R.drawable.flag_finnish
        else R.drawable.flag_english
    )

    SetButton(isDarkMode, appColors, text, onToggleLanguage, icon)
}
