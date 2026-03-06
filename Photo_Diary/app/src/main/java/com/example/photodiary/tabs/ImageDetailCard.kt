package com.example.photodiary.tabs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.photodiary.TitleCard
import com.example.photodiary.colors.AppColors
import com.example.photodiary.database.viewmodel.DatabaseViewModel
import com.example.photodiary.database.DiaryItem
import com.example.photodiary.language.TextBlocks
import com.example.photodiary.location.getLocationName
import java.io.File
import java.util.Locale


/**
 * Composable card for ImageDetail location screen.
 * Used for displaying all the information of the given image.
 * Shows the image, title, description, and weather information.
 *
 * @param appColors Current color palette.
 * @param appLanguage Current language texts.
 * @param itemID ID of the diary item to be displayed.
 * @param viewModel [DatabaseViewModel] providing diary entries.
 */
@Composable
fun ImageDetailCard(
    appColors: AppColors, appLanguage: TextBlocks,
    itemID: Int, viewModel: DatabaseViewModel, isEnglish: Boolean
) {

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

    _root_ide_package_.com.example.photodiary.SetTabLayout(appColors) {
        SetBody(appColors, appLanguage, diaryItem!!, isEnglish)
    }
}


/**
 * Sets up the body for the ImageDetail destination screen.
 * Shows the image, title, description, and weather information.
 *
 * @param appColors Current color palette.
 * @param appLanguage Current language texts.
 * @param diaryItem DiaryItem object to be displayed.
 */
@Composable
private fun SetBody(
    appColors: AppColors, appLanguage: TextBlocks,
    diaryItem: DiaryItem, isEnglish: Boolean
) {
    // Formatting for info cards
    val cardStyle = _root_ide_package_.com.example.photodiary.AppCardStyle(
        colors = CardDefaults.cardColors(
            containerColor = appColors.cardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 20.dp
        ),
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .border(
                3.dp,
                appColors.cardBorder,
                RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
    )

    var cityName by remember { mutableStateOf<String?>("Oulu") }
    var countryName by remember { mutableStateOf<String?>("Finland") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.TopCenter
    ) {
        TitleCard(appColors, appLanguage.details, 6.dp, 0.dp, true)

        _root_ide_package_.com.example.photodiary.SetDefaultColumn(
            PaddingValues(top = 40.dp, bottom = 40.dp, start = 20.dp, end = 20.dp)
        ) {
            // Image
            DisplayImage(appColors, diaryItem)

            // Title
            Spacer(Modifier.height(20.dp))
            SetInfoDisplayCard(appColors, appLanguage.add_info_title, diaryItem.title, cardStyle)

            // Description
            if (diaryItem.description != null && diaryItem.description.isNotBlank()) {
                Spacer(Modifier.height(20.dp))
                SetInfoDisplayCard(
                    appColors,
                    appLanguage.add_info_description,
                    diaryItem.description,
                    cardStyle
                )
            }

            // Weather
            if (diaryItem.weather != null) {
                Spacer(Modifier.height(20.dp))

                // Get location name
                val context = LocalContext.current
                val latitude = diaryItem.latitude
                val longitude = diaryItem.longitude
                getLocationName(
                    latitude, longitude, context,
                    { cityName = it }, { countryName = it },
                    if (isEnglish) Locale.ENGLISH else Locale.forLanguageTag("fi")
                )
                val locationName = "$cityName, $countryName"

                SetWeatherCard(
                    locationName,
                    diaryItem.temperature,
                    diaryItem.weatherIcon,
                    appColors,
                    appLanguage,
                    cardStyle
                )
            }
        }
    }
}


/**
 * Displays the image of the given diary item inside a styled box.
 *
 * @param appColors Current color palette.
 * @param diaryItem DiaryItem object to be displayed.
 */
@Composable
fun DisplayImage(appColors: AppColors, diaryItem: DiaryItem) {
    val context = LocalContext.current
    val imageFile = File(context.filesDir, diaryItem.imageName)

    Box(
        modifier = Modifier.border(2.dp, appColors.cardBorder)
    ) {
        AsyncImage(
            model = imageFile,
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
    }
}


/**
 * Displays a text field for showing the given information.
 *
 * @param appColors Current color palette.
 * @param title Title label of the information card.
 * @param text Text content to display.
 * @param cardStyle Styling configuration for the card.
 */
@Composable
private fun SetInfoDisplayCard(
    appColors: AppColors,
    title: String, text: String, cardStyle: com.example.photodiary.AppCardStyle,
) {
    _root_ide_package_.com.example.photodiary.SetCardLayout(
        appColors = appColors,
        title = title,
        cardStyle = cardStyle,
        contentPadding = PaddingValues(top = 30.dp, bottom = 30.dp, start = 10.dp, end = 10.dp)
    ) {
        Text(
            text = text,
            color = appColors.mainText
        )
    }
}
