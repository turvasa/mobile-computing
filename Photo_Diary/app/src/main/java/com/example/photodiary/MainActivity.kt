package com.example.photodiary

import android.content.Context
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE
import android.hardware.SensorManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.viewmodel.compose.viewModel
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


//private lateinit var sensorManager: SensorManager




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

    // Diary DAO setup
    val diaryItemDAO = db.diaryItemDao()
    val viewModel: DatabaseMethods = viewModel(
        factory = DatabaseMethodsFactory(diaryItemDAO)
    )

    /*
    // Sensor manager
    sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    val temperatureSensor: Sensor? = (
        if (sensorManager.getSensorList(TYPE_AMBIENT_TEMPERATURE) != null) {
            sensorManager.getDefaultSensor(TYPE_AMBIENT_TEMPERATURE)
        }
        else null
    )
     */


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
            viewModel, innerPadding
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
    isDarkMode: Boolean, isEnglish: Boolean,
    onToggleDarkMode: () -> Unit, onToggleLanguage: () -> Unit,
    appColors: AppColors, appLanguage: TextBlocks,
    viewModel: DatabaseMethods, innerPadding: PaddingValues
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
            HomeCard(appColors, appLanguage, viewModel, navController)
        }

        // Add
        composable(AppDestinations.ADD.route) {
            AddNewCard(isDarkMode, appColors, appLanguage, viewModel)
        }

        // Detailed image
        composable("imageDetail/{id}") { entry ->
            val item = entry.arguments?.getString("id")!!.toInt()
            ImageDetailCard(appColors, appLanguage, item, viewModel)
        }

        // Edit image
        composable("imageEditDetail/{id}") { entry ->
            val item = entry.arguments?.getString("id")!!.toInt()
            ImageEditDetailCard(isDarkMode, appColors, appLanguage, item, viewModel)
        }
    }
}




// ------------------
// - General layout -
// ------------------


data class AppCardStyle(
    val colors: CardColors,
    val elevation: CardElevation,
    val modifier: Modifier
)

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
        modifier = Modifier.border(
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
