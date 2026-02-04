package com.example.photodiary

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

        TitleCard(appColors, appLanguage.title_add, 6.dp, 0.dp, true)

        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.9f),
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
                .border(
                    2.dp,
                    appColors.primary2,
                    RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))

            // Diary Item information
            var title by remember { mutableStateOf("") }
            var description by remember { mutableStateOf("") }
            var imagePath by remember { mutableStateOf("") }
            var imageUri by remember { mutableStateOf<Uri?>(null) }
            //var temperature by remember { mutableStateOf("") }
            //var location by remember { mutableStateOf("") }

            SetInfoCard(
                appColors, appLanguage,
                colors, elevation, modifier,
                title, description,
                { title = it }, {description = it}
            )
            SetImageGetter(
                appColors, appLanguage,
                colors, elevation, modifier,
                imagePath, imageUri,
                { imagePath = it }, {imageUri = it}
            )
            SetAddCard(isDarkMode, appColors, appLanguage, diaryItemDAO, colors, elevation, modifier)
        }
    }
}




// -------------
// - Fill info -
// -------------


@Composable
fun SetInfoCard(
    appColors: AppColors, appLanguage: TextBlocks,
    colors: CardColors, elevation: CardElevation, modifier: Modifier,
    title: String, description: String,
    toggleTitle: (String) -> Unit, toggleDescription: (String) -> Unit
) {
    ElevatedCard(
        colors = colors,
        elevation = elevation,
        modifier = modifier.fillMaxHeight(0.4f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TitleCard(appColors, appLanguage.add_info, 15.dp, 2.dp, false)
            SetInfoInputs(appColors, appLanguage, title, description, toggleTitle, toggleDescription)
        }
    }
}


@Composable
fun SetInfoInputs(
    appColors: AppColors, appLanguage: TextBlocks,
    title: String, description: String,
    toggleTitle: (String) -> Unit, toggleDescription: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {

        // Title
        SetInfoInput(
            0.3f, title,
            toggleTitle, 1,
            appLanguage.add_info_title,
            "The Scenery",
            appColors
        )

        // Description
        SetInfoInput(
            0.75f, description,
            toggleDescription, 3,
            appLanguage.add_info_description,
            "Lorem ipsum dolor sit amet...",
            appColors
        )
    }
}


@Composable
fun SetInfoInput(height: Float, value: String, toggle: (String) -> Unit, maxLines: Int, label: String, placeholder: String, appColors: AppColors) {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(height),
        value = value,
        onValueChange = toggle,
        maxLines = maxLines,
        label = { Text(
            text = label,
            color = appColors.secondary3
        )},
        placeholder = { Text(
            text = placeholder,
            color = appColors.secondaryText,
            fontSize = 13.sp
        )},
        textStyle = TextStyle(
            fontSize = 13.sp,
            color = appColors.secondaryText
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = appColors.secondary2.copy(alpha = 0.6f),
            errorBorderColor = Color.Red
        )
    )
}




// --------------------------------
// - Add image file or take photo -
// --------------------------------


@Composable
fun SetImageGetter(
    appColors: AppColors, appLanguage: TextBlocks,
    colors: CardColors, elevation: CardElevation, modifier: Modifier,
    imagePath: String, imageuri: Uri?,
    toggleImagePath: (String) -> Unit, toggleImageUri: (Uri?) -> Unit
) {
    ElevatedCard(
        colors = colors,
        elevation = elevation,
        modifier = modifier.fillMaxHeight(0.5f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TitleCard(appColors, appLanguage.add_file, 15.dp, 2.dp, false)
        }
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




// -------------------
// - Add to database -
// -------------------


@Composable
fun SetAddCard(isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks, diaryItemDAO: DiaryItemDAO, colors: CardColors, elevation: CardElevation, modifier: Modifier) {
    ElevatedCard(
        colors = colors,
        elevation = elevation,
        modifier = modifier.fillMaxHeight(0.8f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TitleCard(appColors, appLanguage.add_new_entry, 15.dp, 2.dp, false)
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

    // Button text
    val text = appLanguage.add_new_entry

    val onClickEvent = { }//DatabaseMethods(diaryItemDAO).addDiaryItem(diaryItem) }

    // Button icon
    val icon = painterResource(
        if (isDarkMode) R.drawable.icon_add_dark
        else R.drawable.icon_add_light
    )

    SetButton(isDarkMode, appColors, text, onClickEvent, icon)
}


@Composable
fun GetDiaryItem() : DiaryItem? {
    return null
}
