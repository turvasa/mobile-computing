package com.example.photodiary

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.room.Database
import java.io.File


private const val errorMessage = "[ERROR] - AddNew: "


@Composable
fun AddNewCard(isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks, diaryItemDAO: DiaryItemDAO) {
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
            SetBody(isDarkMode, appColors, appLanguage, diaryItemDAO)
        }
    }
}


@Composable
fun SetBody(isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks, diaryItemDAO: DiaryItemDAO) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        TitleCard(appColors, appLanguage.title_settings, 6.dp, 0.dp)

        Column(
            modifier = Modifier.fillMaxSize(0.8f),
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
                .fillMaxWidth()
                .height(150.dp)
                .border(
                    2.dp,
                    appColors.primary2,
                    RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))

            SetAddCard(isDarkMode, appColors, appLanguage, diaryItemDAO)
        }
    }
}



@Composable
fun SetAddCard(isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks, diaryItemDAO: DiaryItemDAO) {
    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = appColors.primary
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .border(
                2.dp,
                appColors.primary2,
                RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TitleCard(appColors, appLanguage.settings_title_theme, 15.dp, 2.dp)
            val demoItem = DiaryItem(
                imagePath = "some/path.jpg",
                title = "My Photo",
                description = "A great memory"
            )
            SetAddButton(isDarkMode,appColors, appLanguage, demoItem, diaryItemDAO)
        }
    }
}



@Composable
fun SetAddButton(isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks, diaryItem: DiaryItem, diaryItemDAO: DiaryItemDAO) {
    // Dark mode button
    Button(
        onClick = {},
        //onClick = { DatabaseMethods(diaryItemDAO).addDiaryItem(diaryItem) },
        colors = ButtonDefaults.buttonColors(
            containerColor = appColors.secondaryText
        )
    ) {

        Text(
            color = appColors.secondary2,
            text = appLanguage.add_new_entry
        )

        Image(
            painter = painterResource(
                if (isDarkMode) R.drawable.icon_add_dark
                else R.drawable.icon_add_light
            ),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(32.dp)
                .padding(start = 2.dp, end = 8.dp)
        )
    }
}



@Composable
fun CheckImageValidity(imagePath: String) {
    // Check path validity
    if (imagePath.isEmpty()) {
        throw IllegalArgumentException(errorMessage + "Image path must be given.") as Throwable
    }

    // Check is valid file
    val file = File(imagePath)
    if (!file.exists()) {
        throw IllegalArgumentException(errorMessage + "Image path don't lead to anything.") as Throwable
    }

    // Check is valid image file (by GPT)
    val options = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeFile(file.absolutePath, options)
    if (options.outWidth <= 0 || options.outHeight <= 0) {
        throw IllegalArgumentException(errorMessage + "Image path don't lead to image file.") as Throwable
    }
}
