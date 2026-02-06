package com.example.photodiary

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import java.io.File

@Composable
fun ImageDetailCard(appColors: AppColors, appLanguage: TextBlocks, itemID: Int, viewModel: DatabaseMethods) {

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

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.background_image),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.8f
        )

        SetBody(appColors, appLanguage, diaryItem!!)
    }
}


@Composable
fun SetBody(appColors: AppColors, appLanguage: TextBlocks, diaryItem: DiaryItem) {
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .wrapContentHeight()
            .padding(top = 30.dp, bottom = 30.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = appColors.primary.copy(alpha = 0.8f)
            ),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight()
                .border(
                    3.dp,
                    appColors.primary2,
                    RoundedCornerShape(10.dp)
                )
                .clip(RoundedCornerShape(10.dp))
        ) {
            SetImageCard(appColors, appLanguage, diaryItem)
        }
    }
}


@Composable
fun SetImageCard(appColors: AppColors, appLanguage: TextBlocks, diaryItem: DiaryItem) {
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
            DisplayImage(appColors, diaryItem)

            // Formatting for info cards
            val colors = CardDefaults.cardColors(
                containerColor = appColors.primary
            )
            val elevation = CardDefaults.cardElevation(
                defaultElevation = 20.dp
            )
            val modifier = Modifier
                .fillMaxWidth(0.9f)
                .border(
                    2.dp,
                    appColors.primary2,
                    RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))

            Spacer(Modifier.height(20.dp))
            SetTitleCard(appColors, appLanguage, colors, elevation, modifier, diaryItem)

            Spacer(Modifier.height(20.dp))
            SetDescriptionCard( appColors, appLanguage, colors, elevation, modifier, diaryItem)
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
fun SetTitleCard(
    appColors: AppColors, appLanguage: TextBlocks,
    colors: CardColors, elevation: CardElevation, modifier: Modifier,
    diaryItem: DiaryItem
) {
    ElevatedCard(
        colors = colors,
        elevation = elevation,
        modifier = modifier.wrapContentHeight()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.wrapContentHeight()
        ) {
            TitleCard(
                appColors, appLanguage.add_info_title,
                15.dp, 2.dp, false
            )
            Text(
                text = diaryItem.title,
                color = appColors.secondary3,
                modifier = Modifier.padding(top = 40.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
            )
        }
    }
}


@Composable
fun SetDescriptionCard(
    appColors: AppColors, appLanguage: TextBlocks,
    colors: CardColors, elevation: CardElevation, modifier: Modifier,
    diaryItem: DiaryItem
) {
    ElevatedCard(
        colors = colors,
        elevation = elevation,
        modifier = modifier.wrapContentHeight()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.wrapContentHeight()
        ) {
            TitleCard(
                appColors, appLanguage.home_description,
                15.dp, 2.dp, false
            )
            Text(
                text = diaryItem.description!!,
                color = appColors.secondary3,
                modifier = Modifier.padding(top = 40.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
            )
        }
    }
}
