package com.example.photodiary

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.ElevatedCard
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


@Composable
fun SettingsCard(isDarkMode: Boolean, onToggleDarkMode: () -> Unit, appColors: ColorPalette) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ElevatedCard(
            colors = CardDefaults.cardColors(
                containerColor = appColors.primary
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .border(
                    3.dp,
                    appColors.primary2,
                    RoundedCornerShape(10.dp)
                )
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.7f)
                .clip(RoundedCornerShape(10.dp))
                .alpha(0.8f)
        ) {
            SetBody()
            SetDarkModeButton(
                isDarkMode = isDarkMode,
                onToggleDarkMode = onToggleDarkMode
            )
        }
    }

}


@Composable
fun SetBody() {

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