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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp


@Composable
fun ImageEditDetailCard(isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks, itemID: Int, viewModel: DatabaseViewModel) {

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
        SetEditBody(isDarkMode, appColors, appLanguage, diaryItem!!, viewModel)
    }
}


@Composable
fun SetEditBody(isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks, diaryItem: DiaryItem, viewModel: DatabaseViewModel) {

    // Formatting for setting cards
    val cardStyle = AppCardStyle(
        colors = CardDefaults.cardColors(containerColor = appColors.primary),
        elevation = CardDefaults.cardElevation(defaultElevation = 20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, appColors.primary2, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
    )

    // Diary Item information
    var title by remember { mutableStateOf(diaryItem.title) }
    var description by remember { mutableStateOf(diaryItem.description) }
    //var temperature by remember { mutableStateOf("") }
    //var location by remember { mutableStateOf("") }

    // Diary Item info errors
    var titleError by remember { mutableStateOf<String?>(null) }
    var descriptionError by remember { mutableStateOf<String?>(null) }
    //var temperatureError by remember { mutableStateOf<String?>(null) }
    //var locationError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.TopCenter
    ) {

        TitleCard(appColors, appLanguage.edit, 6.dp, 0.dp, true)

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 40.dp, bottom = 40.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DisplayImage(appColors, diaryItem)

            // Title
            Spacer(Modifier.height(20.dp))
            SetEditInfoCard(
                appColors, appLanguage,
                appLanguage.add_info_title, title,
                "The Scenery", titleError, appLanguage.add_info_title,
                1, { title = it; titleError = null },
                cardStyle
            )

            // Description
            Spacer(Modifier.height(20.dp))
            SetEditInfoCard(
                appColors, appLanguage,
                appLanguage.add_info_description, description!!,
                "Lorem ipsum dolor sit amet...", descriptionError, appLanguage.add_info_description,
                3, { description = it; descriptionError = null },
                cardStyle
            )

            // Publish changes button
            Spacer(Modifier.height(20.dp))
            SetUpdateCard(
                isDarkMode, appColors, appLanguage,
                cardStyle,
                diaryItem, title, description!!,
                {titleError = appLanguage.error_mandatory_field},
                viewModel
            )

            // Delete button
            Spacer(Modifier.height(20.dp))
            SetDeleteCard(
                isDarkMode, appColors, appLanguage,
                cardStyle, diaryItem, viewModel
            )
        }
    }
}


@Composable
fun SetEditInfoCard(
    appColors: AppColors, appLanguage: TextBlocks,
    title: String, value: String, placeholder: String, error: String?,
    label: String, maxLines: Int, changeInfo: (String) -> Unit,
    cardStyle: AppCardStyle,
) {
    SetCardLayout(
        appColors = appColors,
        title = title,
        cardStyle = getCorrectCardStyle(cardStyle, (error != null)),
        contentPadding = PaddingValues(top = 40.dp, bottom = 10.dp, start = 10.dp, end = 10.dp)
    ) {
        SetInfoInput(
            value,
            changeInfo, maxLines,
            label, placeholder,
            error, appColors
        )

        if (error != null) {
            DisplayErrorMessage(error)
        }
    }
}




// ----------
// - Update -
// ----------


@Composable
fun SetUpdateCard(
    isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks,
    cardStyle: AppCardStyle,
    diaryItem: DiaryItem, title: String, description: String,
    titleError: (String) -> Unit, viewModel: DatabaseViewModel
) {
    SetCardLayout(
        appColors = appColors,
        title = appLanguage.update,
        cardStyle = cardStyle
    ) {
        SetUpdateButton(
            isDarkMode, appColors, appLanguage,
            diaryItem, title, description,
            titleError,
            viewModel
        )
    }
}


@Composable
fun SetUpdateButton(
    isDarkMode: Boolean,
    appColors: AppColors, appLanguage: TextBlocks,
    diaryItem: DiaryItem, title: String, description: String,
    titleError: (String) -> Unit, viewModel: DatabaseViewModel
) {

    // Button text
    val text = appLanguage.update

    // Button click event
    val onClickEvent = getUpdateOnClickEvent(
        appLanguage,
        diaryItem, title, description,
        titleError, viewModel
    )

    // Button icon
    val icon = painterResource(
        if (isDarkMode) R.drawable.icon_add_dark
        else R.drawable.icon_add_light
    )

    SetButton(isDarkMode, appColors, text, onClickEvent, icon)
}


fun getUpdateOnClickEvent(
    appLanguage: TextBlocks,
    diaryItem: DiaryItem, title: String, description: String,
    titleError: (String) -> Unit,
    viewModel: DatabaseViewModel
): () -> Unit {
    return {
        var hasError = false

        // Check title validity
        if (title.isEmpty()) {
            titleError(appLanguage.error_mandatory_field)
            hasError = true
        }

        // If everything is ok, send the info to database
        if (!hasError) {
            val updatedDiaryItem = DiaryItem(
                id = diaryItem.id,
                title = title,
                description = description,
                imageName = diaryItem.imageName,
                temperature = diaryItem.temperature,
                weather = diaryItem.weather,
                locationName = diaryItem.locationName
            )
            viewModel.updateDiaryItem(updatedDiaryItem)
        }
    }
}




// ----------
// - Delete -
// ----------


@Composable
fun SetDeleteCard(
    isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks,
    cardStyle: AppCardStyle, diaryItem: DiaryItem, viewModel: DatabaseViewModel
) {
    SetCardLayout(
        appColors = appColors,
        title = appLanguage.delete,
        cardStyle = cardStyle
    ) {
        SetDeleteButton(
            isDarkMode, appColors, appLanguage,
            diaryItem, viewModel
        )
    }
}

@Composable
fun SetDeleteButton(
    isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks,
    diaryItem: DiaryItem, viewModel: DatabaseViewModel
) {

    // Button text
    val text = appLanguage.delete

    // Button click event
    val onClickEvent = { viewModel.deleteDiaryItem(diaryItem) }

    // Button icon
    val icon = painterResource(
        if (isDarkMode) R.drawable.icon_add_dark
        else R.drawable.icon_delete_light
    )

    SetButton(isDarkMode, appColors, text, onClickEvent, icon)
}
