package com.example.mobile_computing_course

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobile_computing_course.ui.theme.MobileComputing_courseTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileComputing_courseTheme {
                MessageCard()
            }
        }
    }
}




@Composable
fun MessageCard() {

    // Boolean variable for the light/dark mode logic
    var isDarkMode by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {

        SetBackgroundImage(isDarkMode)
        SetBody(isDarkMode = isDarkMode)

        // Dark mode button
        Button(
            onClick = { isDarkMode = !isDarkMode },
            modifier = Modifier
                .padding(top = 40.dp)
        ) {
            Text(
                if (isDarkMode) "Light Mode" else "Dark Mode"
            )
        }
    }
}


@Composable
fun SetBackgroundImage(isDarkMode: Boolean) {

    // Background, isDarkMode variable decides which one is used
    Image(
        painter = painterResource(
            if (isDarkMode) R.drawable.background_black else R.drawable.background_white
        ),
        contentDescription = null,
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}


@Composable
fun SetBody(isDarkMode: Boolean) {
    // Body
    Column (
        modifier = Modifier
            .verticalScroll(rememberScrollState()) // Makes the column scrollable
            .padding(top = 200.dp, bottom = 200.dp),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        // Title
        Text(
            "Welcome back!",
            style = MaterialTheme.typography.titleLarge,
            color = if (isDarkMode) Color.White else Color.Black
        )

        // Text
        Text(
            "What took you so long?",
            style = MaterialTheme.typography.bodyMedium,
            color = if (isDarkMode) Color.White else Color.Black
        )

        // Add scrollable meme image
        Spacer(modifier = Modifier.height(700.dp))
        Image (
            painter = painterResource(R.drawable.you_are_the_meme),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MobileComputing_courseTheme {
        MessageCard()
    }
}