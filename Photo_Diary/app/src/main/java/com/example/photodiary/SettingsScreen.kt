package com.example.photodiary

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.ElevatedCard
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp


@Composable
fun SettingsCard(isDarkMode: Boolean, isEnglish: Boolean, onToggleDarkMode: () -> Unit, onToggleLanguage: () -> Unit, appColors: ColorPalette) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = appColors.primary.copy(alpha = 0.8f)
            ),
            modifier = Modifier
                .border(
                    3.dp,
                    appColors.primary2,
                    RoundedCornerShape(10.dp)
                )
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.7f)
                .clip(RoundedCornerShape(10.dp)),
        ) {
            SetBody(isDarkMode, isEnglish, onToggleDarkMode, onToggleLanguage, appColors)
        }
    }

}


@Composable
fun SetBody(isDarkMode: Boolean, isEnglish: Boolean, onToggleDarkMode: () -> Unit, onToggleLanguage: () -> Unit, appColors: ColorPalette) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.5f)
                .padding(top = 40.dp)
        ) {
            SetDarkModeCard(isDarkMode, onToggleDarkMode, appColors)
        }

        /*
        Row(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.5f)
                .padding(bottom = 40.dp)
        ) {
            SetLanguageCard(isEnglish, onToggleDarkMode, onToggleLanguage, appColors)
        }
        */
    }


}



@Composable
fun SetDarkModeCard(isDarkMode: Boolean, onToggleDarkMode: () -> Unit, appColors: ColorPalette) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = appColors.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        modifier = Modifier
            .fillMaxSize()
            .border(
                2.dp,
                appColors.primary2,
                RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(20.dp))
    ) {

        Text(
            "Theme",
            color = appColors.primaryText
        )

        SetDarkModeButton(isDarkMode, onToggleDarkMode)
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
fun SetLanguageCard(isDarkMode: Boolean, onToggleDarkMode: () -> Unit, onToggleLanguage: () -> Unit, appColors: ColorPalette) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = appColors.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        modifier = Modifier
            .fillMaxSize()
            .border(
                2.dp,
                appColors.primary2,
                RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(20.dp))
    ) {

        Text(
            "Language",
            color = appColors.primaryText
        )

        SetLanguageButton(isDarkMode, onToggleLanguage )
    }
}


@Composable
fun SetLanguageButton(isEnglish: Boolean, onToggleLanguage: () -> Unit) {
    // Dark mode button
    Button(
        onClick = onToggleLanguage,
        modifier = Modifier
            .padding(start = 20.dp)
    ) {
        //Icon(
        //    imageVector = if (isDarkMode) Icons.Default.Light
        //)
        Text(
            if (isEnglish) "TODO:English" else "TODO: Suomi"
        )
    }
}
