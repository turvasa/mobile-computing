package com.example.photodiary

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.photodiary.ui.theme.PhotoDiaryTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PhotoDiaryTheme {
                PhotoDiaryApp()
            }
        }
    }
}




// ----------------
// - Destinations -
// ----------------


// App destinations
enum class AppDestinations(
    val label: String,
    var icon: Int
) {
    SETTINGS("Settings", R.drawable.icon_settings_light),
    HOME("Home", R.drawable.icon_home_light),
    ADD("Add", R.drawable.icon_add_light),
}




// ----------
// - Colors -
// ----------

// The colors are AI generated


// Color palette for the app
interface ColorPalette {
    val primary: Color
    val primary2: Color

    val secondary: Color
    val secondary2: Color
    val secondary3: Color

    val primaryText: Color
    val secondaryText: Color

    val border: Color
}


// Light mode colors
object LightModeColors : ColorPalette {
    override val primary = Color(0xFF2C3A59) // Midnight Blue
    override val primary2 = Color(0xFF7477B9) // Purple

    override val secondary = Color(0xFFF2C6C2) // Pink
    override val secondary2 = Color(0xFFE6D8A2) // Gold
    override val secondary3 = Color(0xFFAEB6C8) // Silver

    override val primaryText = Color(0xFF1F2937) // Greyish Blue
    override val secondaryText = Color(0xFF4B5563) // Grey

    override val border = Color(0xFF1E293B) // Dark Blue
}


// Dark mode colors
object DarkModeColors : ColorPalette {
    override val primary = Color(0xFF0F172A) // Deep Blue
    override val primary2 = Color(0xFF8B8CF6) // Purple

    override val secondary = Color(0xFFE5A6C8) // Pink
    override val secondary2 = Color(0xFF9CA3AF) // Silver
    override val secondary3 = Color(0xFF4FD1C5) // Turquoise

    override val primaryText = Color(0xFFE5E7EB) // White
    override val secondaryText = Color(0xFF9CA3AF) // Light Grey

    override val border = Color(0xFFCBD5E1) // Light
}




// ---------
// - Logic -
// ---------


@PreviewScreenSizes
@Composable
fun PhotoDiaryApp() {

    // Navigation control
    val navController = rememberNavController()

    // Boolean variable for the navigation
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.HOME) }

    // Boolean variable for the light/dark mode logic
    var isDarkMode by remember { mutableStateOf(false) }
    SetNavIcons(isDarkMode)

    // Language setting
    var isEnglish by remember { mutableStateOf(true) }

    // App colors. This value will change automatically
    val appColors: ColorPalette = if (isDarkMode) DarkModeColors else LightModeColors

    println("Variables assign.")

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
            appColors,
            currentDestination,
            onDestinationChange = { currentDestination = it } ) }
    ) { innerPadding ->

        println("Navigation bar initialized")

        // Background
        SetBackgroundImage(isDarkMode, currentDestination)

        println("Background initialized")

        // Body
        SetBodyCard(
            navController = navController,
            isEnglish = isEnglish,
            isDarkMode = isDarkMode,
            onToggleDarkMode = { isDarkMode = !isDarkMode },
            onToggleLanguage = { isEnglish = !isEnglish },
            appColors = appColors,
            innerPadding = innerPadding
        )

        println("Body initialized")
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
        if (isDarkMode) R.drawable.icon_add_dark
        else R.drawable.icon_add_light
    )
}



@Composable
fun SetNavBar(navController: NavController, appColors: ColorPalette, currentDestination: AppDestinations, onDestinationChange: (AppDestinations) -> Unit) {
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
                selected = (it == currentDestination),
                onClick = {

                    // Switch the destination (if it is new one)
                    if (navController.currentDestination?.route != it.label) {
                        navController.navigate(it.label) {

                            // Clear the destination stack, when arriving to HOME screen
                            if (it == AppDestinations.HOME) {
                                popUpTo(AppDestinations.HOME.label) { inclusive = true }
                            }
                        }

                        onDestinationChange(it)
                    }
                }
            )
        }
    }
}



@Composable
fun SetBackgroundImage(isDarkMode: Boolean, currentDestination: AppDestinations) {

    // Set the background
    val background: Int = (
        if (currentDestination == AppDestinations.ADD) {
            R.drawable.background_add
        }

        else {
            if (isDarkMode) R.drawable.background_black else R.drawable.background_white
        }
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
fun SetBodyCard(navController: NavHostController, isDarkMode: Boolean, isEnglish: Boolean, onToggleDarkMode: () -> Unit, onToggleLanguage: () -> Unit, appColors: ColorPalette, innerPadding: PaddingValues) {
    NavHost(
        navController,
        startDestination = AppDestinations.HOME.label,
        modifier = Modifier.padding(innerPadding)
    ) {

        // Settings
        composable(AppDestinations.SETTINGS.label) {
            SettingsCard(isDarkMode, isEnglish, onToggleDarkMode, onToggleLanguage, appColors)
        }

        // Home
        composable(AppDestinations.HOME.label) {
            HomeCard(appColors)
        }

        // Add
        composable(AppDestinations.ADD.label) {
            AddNewCard(appColors)
        }
    }
}




// -----------
// - Preview -
// -----------


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PhotoDiaryTheme {
        PhotoDiaryApp()
    }
}
