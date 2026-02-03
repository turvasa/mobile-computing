package com.example.photodiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.photodiary.ui.theme.PhotoDiaryTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Database
        val app = application as PhotoDiaryApplication
        val db = app.db

        enableEdgeToEdge()
        setContent {
            PhotoDiaryTheme {
                PhotoDiaryApp(db)
            }
        }
    }
}




// ----------------
// - Destinations -
// ----------------

enum class AppDestinations(
    var label: String,
    val route: String,
    var icon: Int
) {
    SETTINGS("Settings","settings", R.drawable.icon_settings_light),
    HOME("Home","home", R.drawable.icon_home_light),
    ADD("Add","add", R.drawable.icon_take_photo_light),
}



// ---------
// - Logic -
// ---------



@Composable
fun PhotoDiaryApp(db: AppDatabase) {

    // Navigation control
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Theme setup
    var isDarkMode by remember { mutableStateOf(false) }
    val appColors: AppColors = if (isDarkMode) ColorsDarkMode() else ColorsLightMode()
    SetNavIcons(isDarkMode)

    // Language setup
    var isEnglish by remember { mutableStateOf(true) }
    val appLanguage: TextBlocks = if (isEnglish) TextEnglish() else TextFinnish()
    SetDestinationLabels(appLanguage)

    // Diary DAO
    val diaryItemDAO = db.diaryItemDao()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            Card(
                colors = CardDefaults.cardColors(containerColor = appColors.secondary),
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(start = 0.dp)
            ) {}
        },
        bottomBar = { SetNavBar(
            navController,
            isDarkMode,
            appColors,
            currentRoute
        )}
    ) { innerPadding ->

        // Background
        SetBackgroundImage(isDarkMode)

        // Body
        SetBodyCard(
            navController = navController,
            isEnglish = isEnglish,
            isDarkMode = isDarkMode,
            onToggleDarkMode = { isDarkMode = !isDarkMode },
            onToggleLanguage = { isEnglish = !isEnglish },
            appColors = appColors,
            appLanguage = appLanguage,
            diaryItemDAO = diaryItemDAO,
            innerPadding = innerPadding
        )
    }
}



@Composable
fun SetNavIcons(isDarkMode: Boolean) {

    // Settings
    AppDestinations.SETTINGS.icon = (
        if (isDarkMode) R.drawable.icon_settings_dark
        else R.drawable.icon_settings_light
    )

    // Home
    AppDestinations.HOME.icon = (
        if (isDarkMode) R.drawable.icon_home_dark
        else R.drawable.icon_home_light
    )

    // Home
    AppDestinations.ADD.icon = (
        if (isDarkMode) R.drawable.icon_take_photo_dark
        else R.drawable.icon_take_photo_light
    )
}



@Composable
fun SetDestinationLabels(appLanguage: TextBlocks) {
    AppDestinations.SETTINGS.label = appLanguage.nav_settings
    AppDestinations.HOME.label = appLanguage.nav_home
    AppDestinations.ADD.label = appLanguage.nav_add
}



@Composable
fun SetNavBar(navController: NavController, isDarkMode: Boolean, appColors: AppColors, currentRoute: String?) {
    return NavigationBar(
        containerColor = appColors.secondary,
    ) {
        AppDestinations.entries.forEach {
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = painterResource(it.icon),
                        contentDescription = it.label,
                        modifier = Modifier.width(24.dp),
                        tint = Color.Unspecified
                    )
                },
                label = { Text(
                    text = it.label,
                    color = appColors.primaryText,
                ) },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = if (isDarkMode) ColorsLightMode().primaryText else ColorsDarkMode().primaryText
                ),
                selected = (it.route == currentRoute),
                onClick = {
                    // Switch the destination (if it is new one)
                    if (navController.currentDestination?.route != it.route) {
                        navController.navigate(it.route) {

                            // Clear the destination stack, when arriving to HOME screen
                            if (it == AppDestinations.HOME) {
                                popUpTo(AppDestinations.HOME.route) { inclusive = true }
                            }
                        }
                    }
                }
            )
        }
    }
}



@Composable
fun SetBackgroundImage(isDarkMode: Boolean) {

    // Set the background
    val background: Int = (
        if (isDarkMode) R.drawable.background_black
        else R.drawable.background_white
    )

    // Background, isDarkMode variable decides which one is used
    Image(
        painter = painterResource(background),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}


@Composable
fun SetBodyCard(
    navController: NavHostController,
    isDarkMode: Boolean,
    isEnglish: Boolean,
    onToggleDarkMode: () -> Unit,
    onToggleLanguage: () -> Unit,
    appColors: AppColors,
    appLanguage: TextBlocks,
    diaryItemDAO: DiaryItemDAO,
    innerPadding: PaddingValues
) {
    NavHost(
        navController,
        startDestination = AppDestinations.HOME.route,
        modifier = Modifier.padding(innerPadding)
    ) {

        // Settings
        composable(AppDestinations.SETTINGS.route) {
            SettingsCard(isDarkMode, isEnglish, onToggleDarkMode, onToggleLanguage, appColors, appLanguage)
        }

        // Home
        composable(AppDestinations.HOME.route) {
            HomeCard(appColors, appLanguage)
        }

        // Add
        composable(AppDestinations.ADD.route) {
            AddNewCard(isDarkMode, appColors, appLanguage, diaryItemDAO)
        }
    }
}




// ----------------------
// - Constant functions -
// ----------------------

@Composable
fun BoxScope.TitleCard(appColors: AppColors, text: String, rounding: Dp, offset: Dp) {
    Card(
        shape = RectangleShape,
        modifier = Modifier
            .align(Alignment.TopStart)
            .offset(x = offset, y = offset)
            .clip(RoundedCornerShape(
                topStart = rounding,
                topEnd = 0.dp,
                bottomStart = 0.dp,
                bottomEnd = rounding
            )),
        colors = CardDefaults.cardColors(appColors.primary2),
    ) {
        Text(
            color = appColors.primaryText,
            fontWeight = FontWeight(700),
            text = text,
            modifier = Modifier.padding(3.dp)
        )
    }
}
