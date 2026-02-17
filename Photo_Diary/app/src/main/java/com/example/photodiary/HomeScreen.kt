package com.example.photodiary

import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import java.io.File


/**
 * Composable card for HOME location screen.
 * Used for displaying the corresponding images of the diary items
 * in the database from the app storage.
 *
 * @param appColors Current color palette.
 * @param appLanguage Current language texts.
 * @param viewModel DatabaseViewModel providing diary entries.
 * @param navController NavController for navigation between screens.
 */
@Composable
fun HomeCard(
    appColors: AppColors, appLanguage: TextBlocks,
    viewModel: DatabaseViewModel, navController: NavHostController
) {
    SetTabLayout(appColors) {
            SetBody(appColors, appLanguage, viewModel, navController)
    }
}


/**
 * Sets up the body for the HOME destination screen.
 * Displays the images in a grid.
 *
 *
 * @param appColors Current color palette.
 * @param appLanguage Current language texts.
 * @param viewModel DatabaseViewModel providing diary entries.
 * @param navController NavController for navigation between screens.
 */
@Composable
private fun SetBody(
    appColors: AppColors, appLanguage: TextBlocks,
    viewModel: DatabaseViewModel, navController: NavHostController
) {
    Box {
        TitleCard(appColors, appLanguage.title_home, 3.dp, 0.dp, true)
        DisplayPhotos(appColors, viewModel, navController)
    }
}


/**
 * Displays a grid of all the images of diary items using LazyVerticalGrid.
 * Each image is clickable for viewing and editing.
 *
 * @param appColors Current color palette.
 * @param viewModel DatabaseViewModel providing diary entries.
 * @param navController NavController to navigate to detail/edit screens.
 */
@Composable
private fun DisplayPhotos(appColors: AppColors, viewModel: DatabaseViewModel, navController: NavHostController) {
    val diaryItems = getImages(viewModel)
    val context = LocalContext.current

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(max = 2000.dp),
        contentPadding = PaddingValues(top = 40.dp, bottom = 40.dp, start = 10.dp, end = 10.dp)
    ) {
        items(diaryItems) {

            // Create image file (won't create duplicates)
            val imageFile = remember(it.imageName) {
                File(context.filesDir, it.imageName)
            }

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
                    .combinedClickable(
                        onClick = {
                            navController.navigate("imageDetail/${it.id}")
                        },
                        onLongClick = {
                            navController.navigate("imageEditDetail/${it.id}")
                        }
                    )
            ) {
                AsyncImage(
                    model = imageFile,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


/**
 * Gets a list of all the current diary items from the database.
 *
 * @param viewModel [DatabaseViewModel] providing diary entries.
 * @return List of [DiaryItem] objects currently stored in the database.
 */
@Composable
private fun getImages(viewModel: DatabaseViewModel) : List<DiaryItem> {
    val diaryItems = viewModel.diaryItems.collectAsState()
    return diaryItems.value
}
