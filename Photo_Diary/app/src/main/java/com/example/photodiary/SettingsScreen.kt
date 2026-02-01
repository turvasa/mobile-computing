package com.example.photodiary

import android.R
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.ElevatedCard
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun SettingsCard(isDarkMode: Boolean, isEnglish: Boolean, onToggleDarkMode: () -> Unit, onToggleLanguage: () -> Unit, appColors: ColorPalette) {
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
            SetBody(isDarkMode, isEnglish, onToggleDarkMode, onToggleLanguage, appColors)
        }
    }

}


@Composable
fun SetBody(isDarkMode: Boolean, isEnglish: Boolean, onToggleDarkMode: () -> Unit, onToggleLanguage: () -> Unit, appColors: ColorPalette) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        TextCard(appColors, "Settings", 6.dp, (-17).dp)

        Column(
            modifier = Modifier.fillMaxSize(),
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
                .fillMaxWidth(0.8f)
                .height(150.dp)
                .border(
                    2.dp,
                    appColors.primary2,
                    RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))

            SetDarkModeCard(isDarkMode, onToggleDarkMode, appColors, colors, elevation, modifier)
            SetLanguageCard(isEnglish, onToggleLanguage, appColors, colors, elevation, modifier)
        }
    }


}



@Composable
fun BoxScope.TextCard(appColors: ColorPalette, text: String, rounding: Dp, offset: Dp) {
    Card(
        shape = RectangleShape,
        modifier = Modifier
            .align(Alignment.TopStart)
            .offset(x = offset, y = offset)
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
fun SetDarkModeCard(isDarkMode: Boolean, onToggleDarkMode: () -> Unit, appColors: ColorPalette, colors: CardColors, elevation: CardElevation, modifier: Modifier) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {


        TextCard(appColors, "Theme", 15.dp, 0.dp)

        ElevatedCard(
            colors = colors,
            elevation = elevation,
            modifier = modifier
        ) {
            SetDarkModeButton(isDarkMode, onToggleDarkMode)
        }
    }
}


@Composable
fun SetDarkModeButton(isDarkMode: Boolean, onToggleDarkMode: () -> Unit) {
    // Dark mode button
    Button(
        onClick = onToggleDarkMode,
    ) {
        //Icon(
        //    imageVector = if (isDarkMode) Icons.Default.Light
        //)
        Text(
            if (isDarkMode) "Light Mode" else "Dark Mode"
        )
    }
}




@Composable
fun SetLanguageCard(isEnglish: Boolean, onToggleLanguage: () -> Unit, appColors: ColorPalette, colors: CardColors, elevation: CardElevation, modifier: Modifier) {
    ElevatedCard(
        colors = colors,
        elevation = elevation,
        modifier = modifier
    ) {

        Text(
            "Language",
            color = appColors.primaryText
        )

        SetLanguageButton(isEnglish, onToggleLanguage)
    }
}


@Composable
fun SetLanguageButton(isEnglish: Boolean, onToggleLanguage: () -> Unit) {
    // Dark mode button
    Button(
        onClick = onToggleLanguage,
        modifier = Modifier
            .padding(start = 20.dp, top = 30.dp)
    ) {
        //Icon(
        //    imageVector = if (isDarkMode) Icons.Default.Light
        //)
        Text(
            if (isEnglish) "TODO:English" else "TODO: Suomi"
        )
    }
}
