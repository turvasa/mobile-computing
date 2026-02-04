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


@Composable
fun HomeCard(appColors: AppColors, appLanguage: TextBlocks) {
    Box(modifier = Modifier.fillMaxSize()) {
        SetBody(appColors, appLanguage)
    }
}


@Composable
fun SetBody(appColors: AppColors, appLanguage: TextBlocks) {
    // Body
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()) // Makes the column scrollable
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
                DisplayPhotos(appColors)
            }
        }
    }
}


@Composable
fun DisplayPhotos(appColors: AppColors) {
    val photos = getPhotos()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 30.dp, bottom = 15.dp, start = 12.dp, end = 12.dp)
    ) {
        items(photos) {
            Box(
                modifier = Modifier
                    .padding(6.dp)
                    .aspectRatio(1f)
                    .border(
                        2.dp,
                        appColors.border,
                        RoundedCornerShape(10.dp)
                    )
                    .clip(RoundedCornerShape(10.dp))
            ) {
                Image(
                    painter = painterResource(it),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@Composable
fun getPhotos() : List<Int> {
    return listOf(
        R.drawable.demo_1,
        R.drawable.demo_2,
        R.drawable.demo_3,
        R.drawable.demo_4,
        R.drawable.demo_5,
        R.drawable.demo_6,
        R.drawable.demo_7,
        R.drawable.demo_8,
        R.drawable.demo_9,
        R.drawable.demo_10,
        R.drawable.demo_11,
        R.drawable.demo_12,
        R.drawable.demo_13,
        R.drawable.demo_14,
    )
}
