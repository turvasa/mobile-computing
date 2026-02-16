package com.example.photodiary

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil3.compose.AsyncImage
import java.io.File
import androidx.camera.core.Preview
import androidx.core.content.FileProvider
import kotlin.contracts.contract


@Composable
fun AddNewCard(isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks, weatherViewModel: WeatherViewModel, databaseViewModel: DatabaseViewModel) {

    clearCache(LocalContext.current)

    SetTabLayout(appColors) {
        SetBody(isDarkMode, appColors, appLanguage, weatherViewModel, databaseViewModel)
    }
}


@Composable
fun SetBody(isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks, weatherViewModel: WeatherViewModel, databaseViewModel: DatabaseViewModel) {
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
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val weather by weatherViewModel.weather.collectAsState(initial = null)
    //var location by remember { mutableStateOf("") }

    // Diary Item info errors
    var titleError by remember { mutableStateOf<String?>(null) }
    var imageUriError by remember { mutableStateOf<String?>(null) }

    // Load the weather
    LaunchedEffect(Unit) {
        weatherViewModel.loadWeather()
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        contentAlignment = Alignment.TopCenter
    ) {

        TitleCard(appColors, appLanguage.title_add, 6.dp, 0.dp, true)

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .padding(top = 40.dp, bottom = 40.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){

            SetInfoCard(
                appColors, appLanguage, cardStyle,
                title, description,
                titleError,
                { title = it; titleError = null },
                { description = it }
            )
            Spacer(Modifier.height(20.dp))

            SetImageGetter(
                isDarkMode,
                appColors, appLanguage, cardStyle,
                imageUri, imageUriError,
                { imageUri = it; imageUriError = null }
            )
            Spacer(Modifier.height(20.dp))

            weather?.let { currentWeather ->
                SetWeatherCard(
                    currentWeather.toString(),
                    appColors, appLanguage, cardStyle
                )
                Spacer(Modifier.height(20.dp))
            }

            SetAddCard(
                isDarkMode, appColors, appLanguage, cardStyle,
                title, description, imageUri, weather,
                { titleError = appLanguage.error_mandatory_field },
                { imageUriError = appLanguage.error_mandatory_field },
                { title = ""; description = ""; imageUri = null },
                (titleError != null || imageUri != null),
                databaseViewModel
            )
        }
    }
}




// ------------
// - Generals -
// ------------


@Composable
fun getCorrectCardStyle(cardStyle: AppCardStyle, isError: Boolean) : AppCardStyle {
    val correctStyle = (
        if (!isError) cardStyle
        else cardStyle.copy(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    2.dp,
                    Color.Red,
                    RoundedCornerShape(20.dp)
                )
                .clip(RoundedCornerShape(20.dp))
        )
    )

    return correctStyle
}


@Composable
fun DisplayErrorMessage(error: String) {
    Spacer(Modifier.height(20.dp))
    Text(
        text = error,
        color = Color.Red,
        fontSize = 13.sp
    )
}


fun clearCache(context: Context) {
    context.cacheDir.listFiles()?.forEach { file ->
        if (file.name.startsWith("temp")) file.delete()
    }
}




// -------------
// - Fill info -
// -------------


@Composable
fun SetInfoCard(
    appColors: AppColors, appLanguage: TextBlocks,
    cardStyle: AppCardStyle,
    title: String, description: String,
    titleError: String?,
    toggleTitle: (String) -> Unit, toggleDescription: (String) -> Unit
) {
    SetCardLayout(
        appColors = appColors,
        title = appLanguage.add_info,
        cardStyle = getCorrectCardStyle(cardStyle, (titleError != null))
    ) {
        SetInfoInputs(
            appColors, appLanguage,
            title, description,
            titleError,
            toggleTitle, toggleDescription
        )
    }
}


@Composable
fun SetInfoInputs(
    appColors: AppColors, appLanguage: TextBlocks,
    title: String, description: String,
    titleError: String?,
    toggleTitle: (String) -> Unit, toggleDescription: (String) -> Unit
) {

    // Title
    SetInfoInput(
        title,
        toggleTitle, 1,
        appLanguage.add_info_title,
        "The Scenery",
        titleError,
        appColors
    )

    // Description
    SetInfoInput(
        description,
        toggleDescription, 3,
        appLanguage.add_info_description,
        "Lorem ipsum dolor sit amet...",
        null,
        appColors
    )
}


@Composable
fun SetInfoInput(
    value: String,
    toggle: (String) -> Unit,
    maxLines: Int, label: String,
    placeholder: String,
    error: String?,
    appColors: AppColors
) {
    val height = 80 * maxLines

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(height.dp),
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
            fontSize = 15.sp
        )},
        textStyle = TextStyle(
            fontSize = 15.sp,
            color = appColors.secondaryText
        ),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = appColors.secondary2.copy(alpha = 0.6f),
            errorBorderColor = Color.Red,
        ),
        supportingText = {
            error?.let { Text(
                text = it,
                color = Color.Red,
                fontSize = 13.sp
            ) }
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
    cardStyle: AppCardStyle,
    imageUri: Uri?, imageUriError: String?,
    toggleImageUri: (Uri?) -> Unit
) {
    SetCardLayout(
        appColors = appColors,
        title = appLanguage.add_file,
        cardStyle = getCorrectCardStyle(cardStyle, (imageUriError != null))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            SetAddFileButton(isDarkMode, appColors, appLanguage, toggleImageUri)
            SetTakeImageButton(isDarkMode, appColors, appLanguage, toggleImageUri)
        }

        // Given Image
        if (imageUri != null) {
            DisplayUriImage(appColors, imageUri)
        }

        // Error message
        if (imageUriError != null) {
            DisplayErrorMessage(imageUriError)
        }
    }
}


@Composable
fun SetAddFileButton(
    isDarkMode: Boolean,
    appColors: AppColors, appLanguage: TextBlocks,
    toggleImageUri: (Uri?) -> Unit
) {

    // Button text
    val text = appLanguage.add_file_choose

    // On Click event
    val pickMedia = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        toggleImageUri(uri)
    }
    val onClickEvent = {
        pickMedia.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    // Button icon
    val icon = painterResource(
        if (isDarkMode) R.drawable.icon_take_photo_light
        else R.drawable.icon_take_photo_dark
    )

    SetButton(isDarkMode, appColors, text, onClickEvent, icon)
}


@Composable
fun SetTakeImageButton(
    isDarkMode: Boolean,
    appColors: AppColors, appLanguage: TextBlocks,
    toggleImageUri: (Uri?) -> Unit
) {
    var tempUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    // Setup camera launcher (phone's camera app)
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { accepted ->

        // Save the uri, if image was accepted
        if (accepted) {
            toggleImageUri(tempUri)
        }
    }

    // Button text
    val text = appLanguage.add_file_take_photo

    // On Click event
    val onClickEvent = { getTakeImageEvent(context, cameraLauncher) { tempUri = it } }

    // Button icon
    val icon = painterResource(
        if (isDarkMode) R.drawable.icon_take_photo_light
        else R.drawable.icon_take_photo_dark
    )

    SetButton(isDarkMode, appColors, text, onClickEvent, icon)
}


fun getTakeImageEvent(context: Context, cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>, toggleTempImageUri: (Uri?) -> Unit) {

    // Create a temporary file in the cache
    val tempFile = File(
        context.cacheDir,
        "temp_${System.currentTimeMillis()}.jpg"
    )

    val tempUri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        tempFile
    )
    toggleTempImageUri(tempUri)
    cameraLauncher.launch(tempUri)
}


@Composable
fun DisplayUriImage(appColors: AppColors, imageUri: Uri) {
    Spacer(Modifier.height(20.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
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
            model = imageUri,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}




// -----------
// - Weather -
// -----------


@Composable
fun SetWeatherCard(
    weatherStr: String,
    appColors: AppColors, appLanguage: TextBlocks,
    cardStyle: AppCardStyle
) {

    Log.e("API", "Weather card started")

    SetCardLayout(
        appColors = appColors,
        title = appLanguage.add_weather,
        cardStyle = cardStyle
    ){
        Text(
            text = weatherStr.ifEmpty { appLanguage.error_not_available },
            fontSize = 18.sp,
            color = appColors.secondary3
        )
    }
}




// -------------------
// - Add to database -
// -------------------


@Composable
fun SetAddCard(
    isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks,
    cardStyle: AppCardStyle,
    title: String, description: String, imageUri: Uri?, weather: Weather?,
    titleError: (String) -> Unit, imageUriError: (String) -> Unit,
    zeroInfoFields: () -> Unit, isError: Boolean,
    viewModel: DatabaseViewModel
) {
    SetCardLayout(
        appColors = appColors,
        title = appLanguage.add_create,
        cardStyle = cardStyle
    ) {
        SetAddButton(
            isDarkMode, appColors, appLanguage,
            title, description, imageUri, weather,
            titleError, imageUriError,
            zeroInfoFields, isError,
            viewModel
        )
    }
}


@Composable
fun SetAddButton(
    isDarkMode: Boolean,
    appColors: AppColors, appLanguage: TextBlocks,
    title: String, description: String, imageUri: Uri?, weather: Weather?,
    titleError: (String) -> Unit, imageUriError: (String) -> Unit,
    zeroInfoFields: () -> Unit, isError: Boolean,
    viewModel: DatabaseViewModel
) {
    // Context
    val context = LocalContext.current

    // Button text
    val text = appLanguage.add_create_new

    // Button click event
    val onClickEvent = getAddOnClickEvent(
        context, appLanguage,
        title, description, imageUri, weather,
        titleError, imageUriError,
        zeroInfoFields, viewModel
    )

    // Button icon
    val icon = painterResource(
        if (isDarkMode) R.drawable.icon_add_dark
        else R.drawable.icon_add_light
    )

    SetButton(isDarkMode, appColors, text, onClickEvent, icon)

    // Display error message
    if (isError) {
        DisplayErrorMessage(appLanguage.error_check)
    }
}



fun getAddOnClickEvent(
    context: Context, appLanguage: TextBlocks,
    title: String, description: String, imageUri: Uri?, weather: Weather?,
    titleError: (String) -> Unit, imageUriError: (String) -> Unit,
    zeroInfoFields: () -> Unit, viewModel: DatabaseViewModel
): () -> Unit {
    return {
        var hasError = false

        // Check title validity
        if (title.isEmpty()) {
            titleError(appLanguage.error_mandatory_field)
            hasError = true
        }

        // Check Uri validity
        if (imageUri == null) {
            imageUriError(appLanguage.error_mandatory_field)
            hasError = true
        }

        // If everything is ok, send the info to database
        if (!hasError) {
            imageUri?.let { nonNullImageUri ->
                val savedImageName = saveTheImage(context, nonNullImageUri)
                val diaryItem = DiaryItem(
                    title = title,
                    description = description,
                    imageName = savedImageName,
                    temperature = weather?.getTemperature(),
                    weather = weather?.getWeather(),
                    locationName = weather?.name
                )
                viewModel.addDiaryItem(diaryItem)
            }

            // Make info files empty
            zeroInfoFields()
        }
    }
}


fun saveTheImage(context: Context, imageUri: Uri) : String {
    val inputStream = context.contentResolver.openInputStream(imageUri)
    val imageName = "img_${System.currentTimeMillis()}.jpg"
    val outputFile = File(context.filesDir, imageName)

    // Copy the image file
    outputFile.outputStream().use { output ->
        inputStream?.copyTo(output)
    }

    clearCache(context)
    inputStream?.close()
    return imageName
}
