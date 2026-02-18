package com.example.photodiary

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.photodiary.ui.theme.PhotoDiaryTheme
import android.Manifest
import android.content.Context
import android.icu.util.Calendar
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.navDeepLink
import java.util.concurrent.TimeUnit
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager



class MainActivity : ComponentActivity() {

    // Request permissions
    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ){ }

    /**
     * Main entry activity of the application.
     * Initializes permissions, notification scheduling,
     * sets up the database instance and launches the Compose UI.
     *
     * @param savedInstanceState Previously saved activity state (if available).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        // Set notification requester
        val preferences = getSharedPreferences("settings", Context.MODE_PRIVATE)
        val hour = preferences.getInt("notification_hour", 20)
        val minutes = preferences.getInt("notification_minutes", 0)
        scheduleDailyNotifications(this, hour, minutes)

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




// -----------------------
// - Daily notifications -
// -----------------------


/**
 * Schedules a daily notification using WorkManager.
 * Calculates the next alarm time and enqueues a one-time work request
 * that is rescheduled every day after previous event.
 *
 * @param context Application context used to access WorkManager.
 * @param hour Target hour for the notification (24h format).
 * @param minutes Target minute for the notification.
 */
private fun scheduleDailyNotifications(context: Context, hour: Int, minutes: Int) {
    val currentTime = Calendar.getInstance()
    val scheduleTime = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minutes)
        set(Calendar.SECOND, 0)
    }

    // Schedule tomorrow when the today's time is passed
    if (scheduleTime.before(currentTime)) {
        scheduleTime.add(Calendar.DAY_OF_MONTH, 1)
    }

    // Build the notification requester
    val delay = scheduleTime.timeInMillis - currentTime.timeInMillis
    val dailyWorkRequest = OneTimeWorkRequestBuilder<DailyNotificationWorker>()
        .setInitialDelay(delay, TimeUnit.MILLISECONDS)
        .addTag("daily_notification")
        .build()

    WorkManager.getInstance(context).enqueueUniqueWork(
        "daily_notification_work",
        ExistingWorkPolicy.REPLACE,
        dailyWorkRequest
    )
}




// -----------------------
// - Location Permission -
// -----------------------


/**
 * Requests location permissions on a runtime.
 * Launches permission requests and triggers callbacks depending
 * on whether all required permissions are granted or not.
 *
 * Source: https://medium.com/@munbonecci/how-to-get-your-location-in-jetpack-compose-f085031df4c1
 *
 * @param onPermissionGranted Callback invoked when permissions are granted.
 * @param onPermissionDenied Callback invoked when permissions are denied.
 */
@Composable
fun RequestLocationPermission(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
) {
    // 1. Create a stateful launcher using rememberLauncherForActivityResult
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap ->
        // 2. Check if all requested permissions are granted
        val arePermissionsGranted = permissionsMap.values.reduce { acc, next ->
            acc && next
        }

        // 3. Invoke the appropriate callback based on the permission result
        if (arePermissionsGranted) {
            onPermissionGranted.invoke()
        } else {
            onPermissionDenied.invoke()
        }
    }

    // 4. Launch the permission request on composition
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }
}




// ----------------
// - Destinations -
// ----------------


/**
 * Navigation destinations used the Navigation Bar.
 * Holds destination label, route and icon resource references.
 *
 * @param label Display label of the destination.
 * @param route Navigation route string.
 * @param icon Drawable resource id used as destination icon.
 * @return [Enum] entry representing a navigation destination.
 */
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


/**
 * Root composable of the application.
 * Sets up navigation, theme and language state, view models,
 * and overall layout.
 *
 * @param db Application database instance used to create the
 * corresponding view model
 */
@Composable
private fun PhotoDiaryApp(db: AppDatabase) {

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

    // Diary DAO setup
    val diaryItemDAO = db.diaryItemDao()
    val databaseViewModel: DatabaseViewModel = viewModel(
        factory = DatabaseViewModelFactory(diaryItemDAO)
    )

    // Weather API view model
    val weatherViewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory()
    )

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
            navController,
            isDarkMode, isEnglish,
            { isDarkMode = !isDarkMode },
            { isEnglish = !isEnglish },
            appColors, appLanguage,
            weatherViewModel, databaseViewModel, innerPadding
        )
    }
}


/**
 * Updates the navigation icons according to the current theme.
 * Switches drawable recourses between dark and ligh mode.
 *
 * @param isDarkMode Indicates if dark theme is active.
 */
@Composable
private fun SetNavIcons(isDarkMode: Boolean) {

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


/**
 * Updates the destination labels based on the selected language.
 *
 * @param appLanguage Text block provider containing localized strings.
 */
@Composable
private fun SetDestinationLabels(appLanguage: TextBlocks) {
    AppDestinations.SETTINGS.label = appLanguage.nav_settings
    AppDestinations.HOME.label = appLanguage.nav_home
    AppDestinations.ADD.label = appLanguage.nav_add
}


/**
 * Displays the bottom navigation bar and handles the navigation actions.
 *
 * @param navController Navigation controller used for route changes.
 * @param isDarkMode Indicates if dark theme is active.
 * @param appColors Current color palette of the app.
 * @param currentRoute Currently active navigation route.
 */
@Composable
private fun SetNavBar(navController: NavController, isDarkMode: Boolean, appColors: AppColors, currentRoute: String?) {
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


/**
 * Displays the background image depending of the current theme.
 *
 * @param isDarkMode Indicates if dark theme is active.
 */
@Composable
private fun SetBackgroundImage(isDarkMode: Boolean) {

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


/**
 * Defines navigation routes and loads the corresponding screen composables.
 * Hosts all application screens inside a NavHost container.
 *
 * @param navController Navigation host controller.
 * @param isDarkMode Indicates if dark theme is active.
 * @param isEnglish Indicates whether English (or Finnish) language is selected.
 * @param onToggleDarkMode Callback for toggling dark mode.
 * @param onToggleLanguage Callback for toggling language.
 * @param appColors Current color palette.
 * @param appLanguage Current localized text provider.
 * @param weatherViewModel ViewModel for weather data.
 * @param databaseViewModel ViewModel for database operations.
 * @param innerPadding Scaffold padding values.
 */
@Composable
private fun SetBodyCard(
    navController: NavHostController,
    isDarkMode: Boolean, isEnglish: Boolean,
    onToggleDarkMode: () -> Unit, onToggleLanguage: () -> Unit,
    appColors: AppColors, appLanguage: TextBlocks,
    weatherViewModel: WeatherViewModel, databaseViewModel: DatabaseViewModel, innerPadding: PaddingValues
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
            HomeCard(appColors, appLanguage, databaseViewModel, navController)
        }

        // Add
        composable(
            AppDestinations.ADD.route,
            deepLinks = listOf(navDeepLink { uriPattern = "photodiary://add" }) // Uri link for notifications
        ) {
            AddNewCard(isDarkMode, appColors, appLanguage, weatherViewModel, databaseViewModel)
        }

        // Detailed image
        composable("imageDetail/{id}") { entry ->
            val item = entry.arguments?.getString("id")!!.toInt()
            ImageDetailCard(appColors, appLanguage, item, databaseViewModel)
        }

        // Edit image
        composable("imageEditDetail/{id}") { entry ->
            val item = entry.arguments?.getString("id")!!.toInt()
            ImageEditDetailCard(isDarkMode, appColors, appLanguage, item, databaseViewModel)
        }
    }
}




// ------------------
// - General layout -
// ------------------


/**
 * Genelar layouts ElevatedCard's reusable styling class.
 * Can be used to lessen the param count of certain functions.
 *
 * @param colors Card color configuration.
 * @param elevation Card elevation values.
 * @param modifier Modifier applied to the card.
 * @return Data object representing card style configuration.
 */
data class AppCardStyle(
    val colors: CardColors,
    val elevation: CardElevation,
    val modifier: Modifier
)


/**
 * Creates a general elevated card layout with title and content slot.
 * Used for all possible card designs inside the tabs.
 *
 * @param appColors Current color palette.
 * @param title Title shown on the top left corner of the card.
 * @param cardStyle Styling configuration for the card.
 * @param contentPadding Padding applied to the content area.
 * @param content Composable content inside the card.
 */
@Composable
fun SetCardLayout(
    appColors: AppColors,
    title: String, cardStyle: AppCardStyle,
    contentPadding: PaddingValues = PaddingValues(top = 40.dp, bottom = 40.dp, start = 20.dp, end = 20.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    ElevatedCard(
        colors = cardStyle.colors,
        elevation = cardStyle.elevation,
        modifier = cardStyle.modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            TitleCard(appColors, title, 15.dp, 2.dp, false)

            Column(
                modifier = Modifier
                    .padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                content = content
            )
        }
    }
}


/**
 * Creates a scrollable tab container with styled card background.
 * Is used for every tab (app locations)
 *
 * @param appColors Current color palette.
 * @param content Composable content inside the card.
 */
@Composable
fun SetTabLayout(
    appColors: AppColors,
    content: @Composable ColumnScope.() -> Unit
) {
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
                    .wrapContentHeight(),
                content = content
            )
        }
    }
}




// ----------------------
// - Constant functions -
// ----------------------


/**
 * Displays a titled corner (top left) label used inside cards.
 *
 * @param appColors Current color palette.
 * @param text Title's text content.
 * @param rounding Corner plate rounding size. Should be same as the card's rounding size.
 * @param offset Offset from the top-left corner.
 * @param isLargeTitle Determines title font size and weight. Tab cards should use True, but otherwise False
 */
@Composable
fun BoxScope.TitleCard(appColors: AppColors, text: String, rounding: Dp, offset: Dp, isLargeTitle: Boolean) {
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
        colors = CardDefaults.cardColors(appColors.primary2)
    ) {
        Text(
            color = appColors.primaryText,
            fontWeight = if (isLargeTitle) FontWeight.W700 else FontWeight.W500,
            fontSize = if (isLargeTitle) 23.sp else 15.sp,
            text = text,
            modifier = Modifier.padding(3.dp)
        )
    }
}


/**
 * Creates a styled button with given text and icon.
 *
 * @param isDarkMode Indicates if dark theme is active.
 * @param appColors Current color palette.
 * @param text Button label text.
 * @param onClickEvent Callback executed when button is pressed.
 * @param icon Painter used as the button icon.
 */
@Composable
fun SetButton(
    isDarkMode: Boolean,
    appColors: AppColors, text: String,
    onClickEvent: () -> Unit, icon: Painter
) {
    Button(
        onClick = onClickEvent,
        colors = ButtonDefaults.buttonColors(
            containerColor = appColors.secondaryText
        ),
        contentPadding = PaddingValues(top = 8.dp, bottom = 8.dp, start = 12.dp, end = 12.dp),
        shape = RoundedCornerShape(25.dp),
        modifier = Modifier
            .wrapContentHeight()
            .border(
                2.dp,
                appColors.border.copy(alpha = 0.8f),
                RoundedCornerShape(25.dp)
            )
    ) {

        Text(
            color = if (isDarkMode) ColorsLightMode().secondary2 else ColorsDarkMode().secondary2,
            text = text
        )

        Image(
            painter = icon,
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(32.dp)
                .padding(start = 4.dp)
        )
    }
}
