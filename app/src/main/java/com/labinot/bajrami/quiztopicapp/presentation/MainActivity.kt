package com.labinot.bajrami.quiztopicapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.labinot.bajrami.quiztopicapp.presentation.navigation.NavigationSetUp
import com.labinot.bajrami.quiztopicapp.presentation.theme.QuizTopicAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
            QuizTopicAppTheme {
                NavigationSetUp()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    QuizTopicAppTheme {

        NavigationSetUp()

    }
}