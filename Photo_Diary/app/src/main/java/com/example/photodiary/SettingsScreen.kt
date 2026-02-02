package com.example.photodiary

import android.view.textclassifier.TextLanguage
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.awaitAll


@Composable
fun SettingsCard(isDarkMode: Boolean, isEnglish: Boolean, onToggleDarkMode: () -> Unit, onToggleLanguage: () -> Unit, appColors: AppColors, appLanguage: TextBlocks) {
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
                .padding(top = 20.dp)
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
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {

        TextCard(appColors, appLanguage.title_settings, 6.dp, (-17).dp, (-17).dp)

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
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



@Composable
fun BoxScope.TextCard(appColors: AppColors, text: String, rounding: Dp, offset_x: Dp, offset_y: Dp) {
    Card(
        shape = RectangleShape,
        modifier = Modifier
            .align(Alignment.TopStart)
            .offset(x = offset_x, y = offset_y)
            .clip(RoundedCornerShape(
                topStart = rounding,
                topEnd = 0.dp,
                bottomStart = 0.dp,
                bottomEnd = rounding
            )),
        colors = CardDefaults.cardColors(appColors.primary2),
    ) {
        Text(
            color = appColors.primaryText,
            fontWeight = FontWeight(700),
            text = text,
            modifier = Modifier.padding(3.dp)
        )
    }
}



@Composable
fun SetDarkModeCard(isDarkMode: Boolean, onToggleDarkMode: () -> Unit, appColors: AppColors, appLanguage: TextBlocks, colors: CardColors, elevation: CardElevation, modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
            .padding(top = 90.dp),
        contentAlignment = Alignment.Center
    ) {

        TextCard(appColors, appLanguage.settings_title_theme, 15.dp, 0.dp, 40.dp)

        ElevatedCard(
            colors = colors,
            elevation = elevation,
            modifier = modifier.align(Alignment.CenterEnd)
        ) {
            SetDarkModeButton(isDarkMode, onToggleDarkMode, appColors, appLanguage)
        }
    }
}


@Composable
fun SetDarkModeButton(isDarkMode: Boolean, onToggleDarkMode: () -> Unit, appColors: AppColors, appLanguage: TextBlocks) {
    // Dark mode button
    Button(
        onClick = onToggleDarkMode,
        colors = ButtonDefaults.buttonColors(
            containerColor = appColors.secondaryText
        )
    ) {
        Image(
            painter = painterResource(
                if (isDarkMode) R.drawable.icon_sun
                else R.drawable.icon_moon
            ),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(32.dp)
                .padding(start = 2.dp, end = 8.dp)
        )
        Text(
            color = if (isDarkMode) ColorsLightMode().secondary2 else ColorsDarkMode().secondary2,
            text = if (isDarkMode) appLanguage.settings_button_theme_light else appLanguage.settings_button_theme_dark
        )
    }
}




@Composable
fun SetLanguageCard(isDarkMode: Boolean, isEnglish: Boolean, onToggleLanguage: () -> Unit, appColors: AppColors, appLanguage: TextBlocks, colors: CardColors, elevation: CardElevation, modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 70.dp)
    ){

        TextCard(appColors, appLanguage.settings_title_language, 15.dp, 0.dp, 0.dp)

        ElevatedCard(
            colors = colors,
            elevation = elevation,
            modifier = modifier.align(Alignment.CenterEnd)
        ) {
            SetLanguageButton(isDarkMode, isEnglish, onToggleLanguage, appColors, appLanguage)
        }
    }


}


@Composable
fun SetLanguageButton(isDarkMode: Boolean, isEnglish: Boolean, onToggleLanguage: () -> Unit, appColors: AppColors, appLanguage: TextBlocks) {
    // Dark mode button
    Button(
        onClick = onToggleLanguage,
        colors = ButtonDefaults.buttonColors(
            containerColor = appColors.secondaryText
        )
    ) {

        Text(
            color = if (isDarkMode) ColorsLightMode().secondary2 else ColorsDarkMode().secondary2,
            text = appLanguage.settings_button_language
        )

        Image(
            painter = painterResource(
                if (isEnglish) R.drawable.flag_finnish
                else R.drawable.flag_english
            ),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(32.dp)
                .padding(start = 2.dp, end = 8.dp)
        )

    }
}
