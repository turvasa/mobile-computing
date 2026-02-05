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




@Composable
fun AddNewCard(isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks, viewModel: DatabaseMethods) {
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
            SetBody(isDarkMode, appColors, appLanguage, viewModel)
        }
    }
}


@Composable
fun SetBody(isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks, viewModel: DatabaseMethods) {
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
            var imageUri by remember { mutableStateOf<Uri?>(null) }
            //var temperature by remember { mutableStateOf("") }
            //var location by remember { mutableStateOf("") }

            // Diary Item info errors
            var titleError by remember { mutableStateOf<String?>(null) }
            var descriptionError by remember { mutableStateOf<String?>(null) }
            var imageUriError by remember { mutableStateOf<String?>(null) }
            //var temperatureError by remember { mutableStateOf<String?>(null) }
            //var locationError by remember { mutableStateOf<String?>(null) }

            SetInfoCard(
                appColors, appLanguage,
                colors, elevation, modifier,
                title, description,
                titleError, descriptionError,
                { title = it; titleError = null },
                { description = it; descriptionError = null }
            )
            SetImageGetter(
                isDarkMode,
                appColors, appLanguage,
                colors, elevation, modifier,
                imageUri, imageUriError,
                { imageUri = it; imageUriError = null }
            )
            SetAddCard(
                isDarkMode, appColors, appLanguage,
                colors, elevation, modifier,
                title, description, imageUri,
                {titleError = appLanguage.error_mandatory_field},
                {descriptionError = appLanguage.error_mandatory_field},
                {imageUriError = appLanguage.error_mandatory_field},
                viewModel
            )
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
    titleError: String?, descriptionError: String?,
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
            SetInfoInputs(
                appColors, appLanguage,
                title, description,
                titleError, descriptionError,
                toggleTitle, toggleDescription
            )
        }
    }
}


@Composable
fun SetInfoInputs(
    appColors: AppColors, appLanguage: TextBlocks,
    title: String, description: String,
    titleError: String?, descriptionError: String?,
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
            titleError,
            appColors
        )

        // Description
        SetInfoInput(
            0.75f, description,
            toggleDescription, 3,
            appLanguage.add_info_description,
            "Lorem ipsum dolor sit amet...",
            descriptionError,
            appColors
        )
    }
}


@Composable
fun SetInfoInput(
    height: Float, value: String,
    toggle: (String) -> Unit,
    maxLines: Int, label: String,
    placeholder: String,
    error: String?,
    appColors: AppColors
) {
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
        isError = error != null,
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
            errorBorderColor = Color.Red,
        ),
        supportingText = {
            error?.let { Text(text = it, color = Color.Red) }
        }
    )
}




// --------------------------------
// - Add image file or take photo -
// --------------------------------


@Composable
fun SetImageGetter(
    isDarkMode: Boolean,
    appColors: AppColors, appLanguage: TextBlocks,
    colors: CardColors, elevation: CardElevation, modifier: Modifier,
    imageUri: Uri?, imageUriError: String?,
    toggleImageUri: (Uri?) -> Unit
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
            SetAddFileButton(
                isDarkMode, appColors, appLanguage,
                imageUri, imageUriError,
                toggleImageUri
            )
        }
    }
}


@Composable
fun SetAddFileButton(
    isDarkMode: Boolean,
    appColors: AppColors, appLanguage: TextBlocks,
    imageuri: Uri?, imageUriError: String?,
    toggleImageUri: (Uri?) -> Unit
) {

    // Button text
    val text = appLanguage.add_file_choose

    val onClickEvent = { }

    // Button icon
    val icon = painterResource(
        if (isDarkMode) R.drawable.icon_add_dark
        else R.drawable.icon_add_light
    )

    SetButton(isDarkMode, appColors, text, onClickEvent, icon)
}




// -------------------
// - Add to database -
// -------------------


@Composable
fun SetAddCard(
    isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks,
    colors: CardColors, elevation: CardElevation, modifier: Modifier,
    title: String, description: String, imageUri: Uri?,
    titleError: (String) -> Unit, descriptionError: (String) -> Unit, imageUriError: (String) -> Unit,
    viewModel: DatabaseMethods
) {
    ElevatedCard(
        colors = colors,
        elevation = elevation,
        modifier = modifier.fillMaxHeight(0.8f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            TitleCard(appColors, appLanguage.add_create, 15.dp, 2.dp, false)

            SetAddButton(
                isDarkMode,appColors, appLanguage,
                title, description, imageUri,
                titleError, descriptionError, imageUriError,
                viewModel
            )
        }
    }
}


@Composable
fun SetAddButton(
    isDarkMode: Boolean,
    appColors: AppColors, appLanguage: TextBlocks,
    title: String, description: String, imageUri: Uri?,
    titleError: (String) -> Unit, descriptionError: (String) -> Unit, imageUriError: (String) -> Unit,
    viewModel: DatabaseMethods
) {

    // Button text
    val text = appLanguage.add_create_new

    // Button click event
    val onClickEvent = getAddOnClickEvent(
        title, description, imageUri,
        titleError, descriptionError, imageUriError,
        viewModel
    )

    // Button icon
    val icon = painterResource(
        if (isDarkMode) R.drawable.icon_add_dark
        else R.drawable.icon_add_light
    )

    SetButton(isDarkMode, appColors, text, onClickEvent, icon)
}


fun getAddOnClickEvent(
    title: String, description: String, imageUri: Uri?,
    titleError: (String) -> Unit, descriptionError: (String) -> Unit, imageUriError: (String) -> Unit,
    viewModel: DatabaseMethods
): () -> Unit {
    return {
        var hasError = false

        // Check title validity
        if (title.isEmpty()) {
            titleError
            hasError = true
        }

        // Check description validity
        if (description.length > 50) {
            descriptionError
            hasError = true
        }

        // Check Uri validity
        if (imageUri == null) {
            imageUriError
            hasError = true
        }

        // If everything is ok, send the info to database
        if (!hasError) {
            imageUri?.let { nonNullUri ->
                val diaryItem = DiaryItem(
                    title = title,
                    description = description,
                    imageUri = nonNullUri.toString()
                )
                viewModel.addDiaryItem(diaryItem)
            }
        }
    }
}
