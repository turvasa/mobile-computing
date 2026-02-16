package com.example.photodiary

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import java.io.File

@Composable
fun ImageDetailCard(appColors: AppColors, appLanguage: TextBlocks, itemID: Int, viewModel: DatabaseViewModel) {

    val diaryItemFlow = remember(itemID) {
        viewModel.getDiaryItemByID(itemID)
    }
    val diaryItem by diaryItemFlow.collectAsState(null)

    // Handle null or loading state for the image
    if (diaryItem == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading...")
        }
        return
    }

    SetTabLayout(appColors) {
        SetBody(appColors, appLanguage, diaryItem!!)
    }
}


@Composable
fun SetBody(appColors: AppColors, appLanguage: TextBlocks, diaryItem: DiaryItem) {
    // Formatting for info cards
    val cardStyle = AppCardStyle(
        colors = CardDefaults.cardColors(
            containerColor = appColors.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        modifier = Modifier
            .fillMaxWidth(0.9f)
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
        TitleCard(appColors, appLanguage.details, 6.dp, 0.dp, true)

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 40.dp, bottom = 40.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Image
            DisplayImage(appColors, diaryItem)

            // Title
            Spacer(Modifier.height(20.dp))
            SetInfoDisplayCard(appColors, appLanguage.add_info_title, diaryItem.title, cardStyle)

            // Description
            if (diaryItem.description != null) {
                Spacer(Modifier.height(20.dp))
                SetInfoDisplayCard(appColors,appLanguage.add_info_description, diaryItem.description, cardStyle)
            }

            // Weather
            if (diaryItem.weather != null) {
                Spacer(Modifier.height(20.dp))
                val weatherStr = Weather.formatWeather(diaryItem.temperature, diaryItem.weather, diaryItem.locationName)
                SetWeatherCard(weatherStr, appColors, appLanguage, cardStyle)
            }
        }
    }
}


@Composable
fun DisplayImage(appColors: AppColors, diaryItem: DiaryItem) {
    val context = LocalContext.current
    val imageFile = File(context.filesDir, diaryItem.imageName)

    Box(
        modifier = Modifier.border(2.dp, appColors.border)
    ) {
        AsyncImage(
            model = imageFile,
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}


@Composable
fun SetInfoDisplayCard(
    appColors: AppColors,
    title: String, text: String, cardStyle: AppCardStyle,
) {
    SetCardLayout(
        appColors = appColors,
        title = title,
        cardStyle = cardStyle,
        contentPadding = PaddingValues(top = 40.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
    ) {
        Text(
            text = text,
            color = appColors.secondary3
        )
    }
}

