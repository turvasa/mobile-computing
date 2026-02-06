package com.example.photodiary

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.photodiary.TitleCard

@Composable
fun ImageCard(appColors: AppColors, appLanguage: TextBlocks, diaryItem: DiaryItem) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.8f
        )
        SetBody(appColors, appLanguage, diaryItem)
    }
}


@Composable
fun SetBody(appColors: AppColors, appLanguage: TextBlocks, diaryItem: DiaryItem) {

    Card(
        colors = CardDefaults.cardColors(
            containerColor = appColors.primary.copy(alpha = 0.7f)
        ),
        modifier = Modifier
            .border(
                3.dp,
                appColors.primary2,
                RoundedCornerShape(10.dp)
            )
            .fillMaxWidth(0.9f)
            .wrapContentHeight()
            .heightIn(max = 2000.dp)
            .clip(RoundedCornerShape(10.dp))
    ) {
        Box {
            TitleCard(appColors, appLanguage.title_home, 3.dp, 0.dp, true)
            DisplayImage(appColors, diaryItem)
        }
    }

    // Body
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            colors = CardDefaults.cardColors(
                containerColor = appColors.primary.copy(alpha = 0.7f)
            ),
            modifier = Modifier
                .border(
                    3.dp,
                    appColors.primary2,
                    RoundedCornerShape(10.dp)
                )
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .heightIn(max = 2000.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Box {
                TitleCard(appColors, appLanguage.title_home, 3.dp, 0.dp, true)
                DisplayImage(appColors, diaryItem)
            }
        }
    }
}


@Composable
fun DisplayImage(appColors: AppColors, diaryItem: DiaryItem) {
    Box(
        modifier = Modifier
            .padding(6.dp)
            .border(
                2.dp,
                appColors.border
            )
    ) {
        AsyncImage(
            model = diaryItem.imagePath,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
