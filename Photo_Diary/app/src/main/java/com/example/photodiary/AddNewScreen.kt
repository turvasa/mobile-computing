package com.example.photodiary

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickVisualMedia
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.magnifier
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toFile
import coil3.compose.AsyncImage
import java.io.File




@Composable
fun AddNewCard(isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks, viewModel: DatabaseMethods) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()) // Makes the column scrollable
                .padding(top = 30.dp, bottom = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = appColors.primary.copy(alpha = 0.8f)
                ),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .border(
                        3.dp,
                        appColors.primary2,
                        RoundedCornerShape(10.dp)
                    )
                    .clip(RoundedCornerShape(10.dp))
                    .wrapContentHeight()
            ) {
                SetBody(isDarkMode, appColors, appLanguage, viewModel)
            }
        }
    }
}


@Composable
fun SetBody(isDarkMode: Boolean, appColors: AppColors, appLanguage: TextBlocks, viewModel: DatabaseMethods) {
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
            // Formatting for setting cards
            val colors = CardDefaults.cardColors(containerColor = appColors.primary)
            val elevation = CardDefaults.cardElevation(defaultElevation = 20.dp)
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

            // Set the body
            SetInfoCard(
                appColors, appLanguage,
                colors, elevation, modifier,
                title, description,
                titleError, descriptionError,
                { title = it; titleError = null },
                { description = it; descriptionError = null }
            )
            Spacer(Modifier.height(20.dp))

            SetImageGetter(
                isDarkMode,
                appColors, appLanguage,
                colors, elevation, modifier,
                imageUri, imageUriError,
                { imageUri = it; imageUriError = null }
            )
            Spacer(Modifier.height(20.dp))

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
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
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
            .padding(top = 40.dp, bottom = 40.dp, start = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
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
            descriptionError,
            appColors
        )
    }
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
                fontSize = 15.sp
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
    colors: CardColors, elevation: CardElevation, modifier: Modifier,
    imageUri: Uri?, imageUriError: String?,
    toggleImageUri: (Uri?) -> Unit
) {
    val context = LocalContext.current
    val errorModifier = Modifier
        .fillMaxWidth()
        .border(
            2.dp,
            Color.Red,
            RoundedCornerShape(20.dp)
        )
        .clip(RoundedCornerShape(20.dp))

    ElevatedCard(
        colors = colors,
        elevation = elevation,
        modifier =
            if (imageUriError == null) modifier
            else errorModifier

    ) {
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.wrapContentHeight()
        ) {
            TitleCard(
                appColors, appLanguage.add_file,
                15.dp, 2.dp, false
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp, bottom = 40.dp, start = 20.dp, end = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SetAddFileButton(isDarkMode, appColors, appLanguage, toggleImageUri)

                // Given Image
                if (imageUri != null) {
                    DisplayUriImage(appColors, imageUri)
                }

                // Error message
                if (imageUriError != null) {
                    DisplayImageErrorMessage(appColors, imageUriError)
                }
            }
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


@Composable
fun DisplayImageErrorMessage(appColors: AppColors, imageUriError: String?) {
    Spacer(Modifier.height(20.dp))

    // Error message
    val error = (
            if (imageUriError != null) imageUriError
            else ""
            )
    Text(
        text = error,
        color = appColors.primary2,
        fontSize = 10.sp
    )
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
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.wrapContentHeight()
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
    // Context
    val context = LocalContext.current

    // Button text
    val text = appLanguage.add_create_new

    // Button click event
    val onClickEvent = getAddOnClickEvent(
        context, appLanguage,
        title, description, imageUri,
        titleError, descriptionError, imageUriError,
        viewModel
    )

    // Button icon
    val icon = painterResource(
        if (isDarkMode) R.drawable.icon_add_dark
        else R.drawable.icon_add_light
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 40.dp, start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        SetButton(isDarkMode, appColors, text, onClickEvent, icon)
    }
}


fun getAddOnClickEvent(
    context: Context, appLanguage: TextBlocks,
    title: String, description: String, imageUri: Uri?,
    titleError: (String) -> Unit, descriptionError: (String) -> Unit, imageUriError: (String) -> Unit,
    viewModel: DatabaseMethods
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
                    imageName = savedImageName
                )
                viewModel.addDiaryItem(diaryItem)
            }
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

    inputStream?.close()
    return imageName
}
