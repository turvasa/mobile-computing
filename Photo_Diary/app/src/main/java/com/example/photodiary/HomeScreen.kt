package com.example.photodiary

import android.net.Uri
import android.provider.ContactsContract
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import kotlinx.coroutines.flow.MutableStateFlow


@Composable
fun HomeCard(appColors: AppColors, appLanguage: TextBlocks, viewModel: DatabaseMethods) {
    Box(modifier = Modifier.fillMaxSize()) {
        SetBody(appColors, appLanguage, viewModel)
    }
}


@Composable
fun SetBody(appColors: AppColors, appLanguage: TextBlocks, viewModel: DatabaseMethods) {
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
                DisplayPhotos(appColors, viewModel)
            }
        }
    }
}


@Composable
fun DisplayPhotos(appColors: AppColors, viewModel: DatabaseMethods) {
    val diaryItems = getImages(viewModel)

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(top = 30.dp, bottom = 15.dp, start = 12.dp, end = 12.dp)
    ) {
        items(diaryItems) {
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
                AsyncImage(
                    model = it.imageUri,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@Composable
fun getImages(viewModel: DatabaseMethods) : List<DiaryItem> {
    val diaryItems = viewModel.diaryItems.collectAsState()
    return diaryItems.value
}
