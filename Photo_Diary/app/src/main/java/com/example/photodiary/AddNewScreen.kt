package com.example.photodiary

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier





@Composable
fun AddNewCard(appColors: AppColors, appLanguage: TextBlocks) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Greeting(appColors, "Add New")
    }
}


@Composable
fun Greeting(appColors: AppColors, name: String) {
    Text(
        text = name,
        color = appColors.primaryText
    )
}
