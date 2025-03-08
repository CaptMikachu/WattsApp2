package com.example.wattsapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wattsapp.ui.theme.WattsAppTheme
import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.draw.clip
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.example.wattsapp.ui.theme.errorContainerLight
import com.example.wattsapp.ui.theme.primaryContainerLight
import com.example.wattsapp.ui.theme.secondaryLight
import android.content.SharedPreferences
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.unit.times
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.example.wattsapp.ui.theme.onBackgroundLight
import com.example.wattsapp.ui.theme.scrimLight
import com.example.wattsapp.ui.theme.tertiaryLight
import java.io.File
import java.time.ZoneId
import kotlin.math.abs


const val BASE_URL = "https://api.porssisahko.net/"
const val LATEST_PRICES_ENDPOINT = "v1/latest-prices.json"
const val API_MAIN_PAGE_URL = "https://www.porssisahko.net/api"


val dummyPrices = listOf(
    Price(5.874, "2024-12-13T07:00:00.000Z", "2024-12-13T08:00:00.000Z"),
    Price(9.457, "2024-12-13T06:00:00.000Z", "2024-12-13T07:00:00.000Z"),
    Price(0.2, "2024-12-13T05:00:00.000Z", "2024-12-13T06:00:00.000Z"),
    Price(10.899, "2024-12-13T04:00:00.000Z", "2024-12-13T05:00:00.000Z"),
    Price(3.172, "2024-12-13T03:00:00.000Z", "2024-12-13T04:00:00.000Z"),
    Price(11.0, "2024-12-13T02:00:00.000Z", "2024-12-13T03:00:00.000Z"),
    Price(0.2, "2024-12-13T01:00:00.000Z", "2024-12-13T02:00:00.000Z"),
    Price(9.113, "2024-12-13T00:00:00.000Z", "2024-12-13T01:00:00.000Z"),
    Price(3.533, "2024-12-12T23:00:00.000Z", "2024-12-13T00:00:00.000Z"),
    Price(3.533, "2024-12-12T22:00:00.000Z", "2024-12-12T23:00:00.000Z"),
    Price(-26.333, "2024-12-12T21:00:00.000Z", "2024-12-12T22:00:00.000Z"),
    Price(0.0, "2024-12-12T20:00:00.000Z", "2024-12-12T21:00:00.000Z"),
    Price(8.757, "2024-12-12T19:00:00.000Z", "2024-12-12T20:00:00.000Z"),
    Price(-10.58, "2024-12-12T18:00:00.000Z", "2024-12-12T19:00:00.000Z"),
    Price(14.761, "2024-12-12T17:00:00.000Z", "2024-12-12T18:00:00.000Z"),
    Price(0.0, "2024-12-12T16:00:00.000Z", "2024-12-12T17:00:00.000Z"),
    Price(25.104, "2024-12-12T15:00:00.000Z", "2024-12-12T16:00:00.000Z"),
    Price(-128.118, "2024-12-12T14:00:00.000Z", "2024-12-12T15:00:00.000Z"),
    Price(20.541, "2024-12-12T13:00:00.000Z", "2024-12-12T14:00:00.000Z"),
    Price(14.761, "2024-12-12T12:00:00.000Z", "2024-12-12T13:00:00.000Z"),
    Price(0.0, "2024-12-12T11:00:00.000Z", "2024-12-12T12:00:00.000Z"),
    Price(25.104, "2024-12-12T10:00:00.000Z", "2024-12-12T11:00:00.000Z"),
    Price(-28.118, "2024-12-12T09:00:00.000Z", "2024-12-12T10:00:00.000Z"),
    Price(20.541, "2024-12-12T08:00:00.000Z", "2024-12-12T09:00:00.000Z")
)

val dummyPrices2 = listOf(
    Price(3.85, "2024-12-13T07:00:00.000Z", "2024-12-13T08:00:00.000Z"),
    Price(4.72, "2024-12-13T06:00:00.000Z", "2024-12-13T07:00:00.000Z"),
    Price(3.45, "2024-12-13T05:00:00.000Z", "2024-12-13T06:00:00.000Z"),
    Price(5.03, "2024-12-13T04:00:00.000Z", "2024-12-13T05:00:00.000Z"),
    Price(3.62, "2024-12-13T03:00:00.000Z", "2024-12-13T04:00:00.000Z"),
    Price(5.2, "2024-12-13T02:00:00.000Z", "2024-12-13T03:00:00.000Z"),
    Price(3.45, "2024-12-13T01:00:00.000Z", "2024-12-13T02:00:00.000Z"),
    Price(4.64, "2024-12-13T00:00:00.000Z", "2024-12-13T01:00:00.000Z"),
    Price(3.85, "2024-12-12T23:00:00.000Z", "2024-12-13T00:00:00.000Z"),
    Price(3.85, "2024-12-12T22:00:00.000Z", "2024-12-12T23:00:00.000Z"),
    Price(3.45, "2024-12-12T21:00:00.000Z", "2024-12-12T22:00:00.000Z"),
    Price(3.45, "2024-12-12T20:00:00.000Z", "2024-12-12T21:00:00.000Z"),
    Price(4.38, "2024-12-12T19:00:00.000Z", "2024-12-12T20:00:00.000Z"),
    Price(3.45, "2024-12-12T18:00:00.000Z", "2024-12-12T19:00:00.000Z"),
    Price(4.92, "2024-12-12T17:00:00.000Z", "2024-12-12T18:00:00.000Z"),
    Price(3.45, "2024-12-12T16:00:00.000Z", "2024-12-12T17:00:00.000Z"),
    Price(5.08, "2024-12-12T15:00:00.000Z", "2024-12-12T16:00:00.000Z"),
    Price(3.45, "2024-12-12T14:00:00.000Z", "2024-12-12T15:00:00.000Z"),
    Price(4.85, "2024-12-12T13:00:00.000Z", "2024-12-12T14:00:00.000Z"),
    Price(4.92, "2024-12-12T12:00:00.000Z", "2024-12-12T13:00:00.000Z"),
    Price(3.45, "2024-12-12T11:00:00.000Z", "2024-12-12T12:00:00.000Z"),
    Price(5.08, "2024-12-12T10:00:00.000Z", "2024-12-12T11:00:00.000Z"),
    Price(3.45, "2024-12-12T09:00:00.000Z", "2024-12-12T10:00:00.000Z"),
    Price(4.85, "2024-12-12T08:00:00.000Z", "2024-12-12T09:00:00.000Z")
)

val dummyPrices3 = listOf(
    // March 7, 2025 (00:00 - 23:00)
    Price(15.74, "2025-03-07T00:00:00.000Z", "2025-03-07T01:00:00.000Z"),
    Price(-9.45, "2025-03-07T01:00:00.000Z", "2025-03-07T02:00:00.000Z"),
    Price(7.32, "2025-03-07T02:00:00.000Z", "2025-03-07T03:00:00.000Z"),
    Price(-12.89, "2025-03-07T03:00:00.000Z", "2025-03-07T04:00:00.000Z"),
    Price(3.17, "2025-03-07T04:00:00.000Z", "2025-03-07T05:00:00.000Z"),
    Price(0.54, "2025-03-07T05:00:00.000Z", "2025-03-07T06:00:00.000Z"),
    Price(-20.58, "2025-03-07T06:00:00.000Z", "2025-03-07T07:00:00.000Z"),
    Price(9.11, "2025-03-07T07:00:00.000Z", "2025-03-07T08:00:00.000Z"),
    Price(-15.33, "2025-03-07T08:00:00.000Z", "2025-03-07T09:00:00.000Z"),
    Price(6.78, "2025-03-07T09:00:00.000Z", "2025-03-07T10:00:00.000Z"),
    Price(-8.92, "2025-03-07T10:00:00.000Z", "2025-03-07T11:00:00.000Z"),
    Price(0.05, "2025-03-07T11:00:00.000Z", "2025-03-07T12:00:00.000Z"),
    Price(10.75, "2025-03-07T12:00:00.000Z", "2025-03-07T13:00:00.000Z"),
    Price(-18.58, "2025-03-07T13:00:00.000Z", "2025-03-07T14:00:00.000Z"),
    Price(14.76, "2025-03-07T14:00:00.000Z", "2025-03-07T15:00:00.000Z"),
    Price(-5.23, "2025-03-07T15:00:00.000Z", "2025-03-07T16:00:00.000Z"),
    Price(25.10, "2025-03-07T16:00:00.000Z", "2025-03-07T17:00:00.000Z"),
    Price(-30.11, "2025-03-07T17:00:00.000Z", "2025-03-07T18:00:00.000Z"),
    Price(20.54, "2025-03-07T18:00:00.000Z", "2025-03-07T19:00:00.000Z"),
    Price(-24.76, "2025-03-07T19:00:00.000Z", "2025-03-07T20:00:00.000Z"),
    Price(0.00, "2025-03-07T20:00:00.000Z", "2025-03-07T21:00:00.000Z"),
    Price(17.32, "2025-03-07T21:00:00.000Z", "2025-03-07T22:00:00.000Z"),
    Price(-14.27, "2025-03-07T22:00:00.000Z", "2025-03-07T23:00:00.000Z"),
    Price(8.64, "2025-03-07T23:00:00.000Z", "2025-03-08T00:00:00.000Z"),

    // March 8, 2025 (00:00 - 23:00)
    Price(15.74, "2025-03-08T00:00:00.000Z", "2025-03-08T01:00:00.000Z"),
    Price(-9.45, "2025-03-08T01:00:00.000Z", "2025-03-08T02:00:00.000Z"),
    Price(7.32, "2025-03-08T02:00:00.000Z", "2025-03-08T03:00:00.000Z"),
    Price(-12.89, "2025-03-08T03:00:00.000Z", "2025-03-08T04:00:00.000Z"),
    Price(3.17, "2025-03-08T04:00:00.000Z", "2025-03-08T05:00:00.000Z"),
    Price(0.54, "2025-03-08T05:00:00.000Z", "2025-03-08T06:00:00.000Z"),
    Price(-20.58, "2025-03-08T06:00:00.000Z", "2025-03-08T07:00:00.000Z"),
    Price(9.11, "2025-03-08T07:00:00.000Z", "2025-03-08T08:00:00.000Z"),
    Price(-15.33, "2025-03-08T08:00:00.000Z", "2025-03-08T09:00:00.000Z"),
    Price(6.78, "2025-03-08T09:00:00.000Z", "2025-03-08T10:00:00.000Z"),
    Price(-8.92, "2025-03-08T10:00:00.000Z", "2025-03-08T11:00:00.000Z"),
    Price(0.05, "2025-03-08T11:00:00.000Z", "2025-03-08T12:00:00.000Z"),
    Price(10.75, "2025-03-08T12:00:00.000Z", "2025-03-08T13:00:00.000Z"),
    Price(-18.58, "2025-03-08T13:00:00.000Z", "2025-03-08T14:00:00.000Z"),
    Price(14.76, "2025-03-08T14:00:00.000Z", "2025-03-08T15:00:00.000Z"),
    Price(-5.23, "2025-03-08T15:00:00.000Z", "2025-03-08T16:00:00.000Z"),
    Price(25.10, "2025-03-08T16:00:00.000Z", "2025-03-08T17:00:00.000Z"),
    Price(-30.11, "2025-03-08T17:00:00.000Z", "2025-03-08T18:00:00.000Z"),
    Price(20.54, "2025-03-08T18:00:00.000Z", "2025-03-08T19:00:00.000Z"),
    Price(-24.76, "2025-03-08T19:00:00.000Z", "2025-03-08T20:00:00.000Z"),
    Price(0.00, "2025-03-08T20:00:00.000Z", "2025-03-08T21:00:00.000Z"),
    Price(17.32, "2025-03-08T21:00:00.000Z", "2025-03-08T22:00:00.000Z"),
    Price(-14.27, "2025-03-08T22:00:00.000Z", "2025-03-08T23:00:00.000Z"),
    Price(8.64, "2025-03-08T23:00:00.000Z", "2025-03-09T00:00:00.000Z")
)

val dummyPrices4 = listOf(
    // March 7, 2025 (00:00 - 23:00)
    Price(5.12, "2025-03-07T00:00:00.000Z", "2025-03-07T01:00:00.000Z"),
    Price(3.45, "2025-03-07T01:00:00.000Z", "2025-03-07T02:00:00.000Z"),
    Price(7.82, "2025-03-07T02:00:00.000Z", "2025-03-07T03:00:00.000Z"),
    Price(2.19, "2025-03-07T03:00:00.000Z", "2025-03-07T04:00:00.000Z"),
    Price(4.67, "2025-03-07T04:00:00.000Z", "2025-03-07T05:00:00.000Z"),
    Price(8.34, "2025-03-07T05:00:00.000Z", "2025-03-07T06:00:00.000Z"),
    Price(1.58, "2025-03-07T06:00:00.000Z", "2025-03-07T07:00:00.000Z"),
    Price(6.91, "2025-03-07T07:00:00.000Z", "2025-03-07T08:00:00.000Z"),
    Price(3.33, "2025-03-07T08:00:00.000Z", "2025-03-07T09:00:00.000Z"),
    Price(9.78, "2025-03-07T09:00:00.000Z", "2025-03-07T10:00:00.000Z"),
    Price(4.92, "2025-03-07T10:00:00.000Z", "2025-03-07T11:00:00.000Z"),
    Price(0.05, "2025-03-07T11:00:00.000Z", "2025-03-07T12:00:00.000Z"),
    Price(7.75, "2025-03-07T12:00:00.000Z", "2025-03-07T13:00:00.000Z"),
    Price(5.28, "2025-03-07T13:00:00.000Z", "2025-03-07T14:00:00.000Z"),
    Price(2.76, "2025-03-07T14:00:00.000Z", "2025-03-07T15:00:00.000Z"),
    Price(6.23, "2025-03-07T15:00:00.000Z", "2025-03-07T16:00:00.000Z"),
    Price(8.10, "2025-03-07T16:00:00.000Z", "2025-03-07T17:00:00.000Z"),
    Price(2.00, "2025-03-07T17:00:00.000Z", "2025-03-07T18:00:00.000Z"),  // Only negative value
    Price(9.54, "2025-03-07T18:00:00.000Z", "2025-03-07T19:00:00.000Z"),
    Price(4.76, "2025-03-07T19:00:00.000Z", "2025-03-07T20:00:00.000Z"),
    Price(1.20, "2025-03-07T20:00:00.000Z", "2025-03-07T21:00:00.000Z"),
    Price(7.32, "2025-03-07T21:00:00.000Z", "2025-03-07T22:00:00.000Z"),
    Price(3.27, "2025-03-07T22:00:00.000Z", "2025-03-07T23:00:00.000Z"),
    Price(5.64, "2025-03-07T23:00:00.000Z", "2025-03-08T00:00:00.000Z"),

    // March 8, 2025 (00:00 - 23:00)
    Price(4.74, "2025-03-08T00:00:00.000Z", "2025-03-08T01:00:00.000Z"),
    Price(2.45, "2025-03-08T01:00:00.000Z", "2025-03-08T02:00:00.000Z"),
    Price(6.32, "2025-03-08T02:00:00.000Z", "2025-03-08T03:00:00.000Z"),
    Price(8.89, "2025-03-08T03:00:00.000Z", "2025-03-08T04:00:00.000Z"),
    Price(-2.00, "2025-03-08T04:00:00.000Z", "2025-03-08T05:00:00.000Z"),
    Price(5.54, "2025-03-08T05:00:00.000Z", "2025-03-08T06:00:00.000Z"),
    Price(7.58, "2025-03-08T06:00:00.000Z", "2025-03-08T07:00:00.000Z"),
    Price(2.11, "2025-03-08T07:00:00.000Z", "2025-03-08T08:00:00.000Z"),
    Price(9.33, "2025-03-08T08:00:00.000Z", "2025-03-08T09:00:00.000Z"),
    Price(4.78, "2025-03-08T09:00:00.000Z", "2025-03-08T10:00:00.000Z"),
    Price(1.92, "2025-03-08T10:00:00.000Z", "2025-03-08T11:00:00.000Z"),
    Price(6.05, "2025-03-08T11:00:00.000Z", "2025-03-08T12:00:00.000Z"),
    Price(3.75, "2025-03-08T12:00:00.000Z", "2025-03-08T13:00:00.000Z"),
    Price(8.58, "2025-03-08T13:00:00.000Z", "2025-03-08T14:00:00.000Z"),
    Price(5.76, "2025-03-08T14:00:00.000Z", "2025-03-08T15:00:00.000Z"),
    Price(2.23, "2025-03-08T15:00:00.000Z", "2025-03-08T16:00:00.000Z"),
    Price(7.10, "2025-03-08T16:00:00.000Z", "2025-03-08T17:00:00.000Z"),
    Price(4.11, "2025-03-08T17:00:00.000Z", "2025-03-08T18:00:00.000Z"),
    Price(9.54, "2025-03-08T18:00:00.000Z", "2025-03-08T19:00:00.000Z"),
    Price(3.76, "2025-03-08T19:00:00.000Z", "2025-03-08T20:00:00.000Z"),
    Price(1.00, "2025-03-08T20:00:00.000Z", "2025-03-08T21:00:00.000Z"),
    Price(6.32, "2025-03-08T21:00:00.000Z", "2025-03-08T22:00:00.000Z"),
    Price(4.27, "2025-03-08T22:00:00.000Z", "2025-03-08T23:00:00.000Z"),
    Price(2.64, "2025-03-08T23:00:00.000Z", "2025-03-09T00:00:00.000Z")
)

class MainActivity : ComponentActivity() {
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        enableEdgeToEdge()
        setContent {
            WattsAppTheme {
                var userName by remember { mutableStateOf(sharedPreferences.getString("user_name", "") ?: "") } // Read the user name from the shared preferences

                WattsApp(
                    sharedPreferences = sharedPreferences,
                    userName = userName,
                    onUserNameChange = { newUserName -> // Update the user name
                        userName = newUserName
                        sharedPreferences.edit().putString("user_name", newUserName).apply()
                    }
                )
            }
        }
    }
}


@Composable
fun WattsApp(sharedPreferences: SharedPreferences, userName: String, onUserNameChange: (String) -> Unit) {
    val navController = rememberNavController()

    Scaffold(
        topBar = {
            TopBar(navController, sharedPreferences)
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        content = { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = "page1",
                modifier = Modifier.padding(innerPadding) // Apply the innerPadding here
            ) {
                composable("page1") {// Home
                    Page1()
                }
                composable("page2") {// Calculator
                    Page2()
                }
                composable("page3") {// Data
                    Page3()
                }
                composable("page4") {// User
                    Page4( userName, onUserNameChange)
                }
            }
        }
    )
}

// Top app bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController, sharedPreferences: SharedPreferences) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val title = when (currentRoute) {
        "page1" -> stringResource(R.string.home_title)
        "page2" -> stringResource(R.string.calculator_page_title)
        "page3" -> stringResource(R.string.title_data_page)
        "page4" -> stringResource(R.string.title_user_page)
        else -> stringResource(R.string.app_name)
    }
    val userName = sharedPreferences.getString("user_name", "") ?: ""

    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Start,
                    fontSize = 25.sp,
                    modifier = Modifier.padding(start= 8.dp, top = 16.dp, end = 16.dp, bottom = 16.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.padding(0.dp, 0.dp, 16.dp, 0.dp)
                ) {
                    if (userName.isNotEmpty()) {
                        Icon(
                            Icons.Filled.Person,
                            contentDescription = "User",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .size(if (userName.length > 5) 12.dp else 24.dp)
                        )
                        Text(
                            text = userName,
                            fontSize = if (userName.length > 5) 10.sp else 15.sp,
                            textAlign = TextAlign.Center

                        )
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface,
            titleContentColor = MaterialTheme.colorScheme.surface
        )
    )
}

// Bottom navigation bar
@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.inverseSurface,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Home, contentDescription = stringResource(R.string.icon_home)) },
            label = { Text(stringResource(R.string.home_nav_button),
                color = MaterialTheme.colorScheme.surface) },
            selected = currentRoute == "page1",
            onClick = {
                navController.navigate("page1") {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.surface
            )
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.baseline_calculate_24), contentDescription = stringResource(
                R.string.icon_calculate
            )
            ) },
            label = { Text(stringResource(R.string.counter_nav_button),
                color = MaterialTheme.colorScheme.surface) },
            selected = currentRoute == "page2",
            onClick = {
                navController.navigate("page2") {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.surface
            )
        )
        NavigationBarItem(
            icon = { Icon(painter = painterResource(id = R.drawable.baseline_data_object_24), contentDescription = stringResource(
                R.string.icon_data
            )
            ) },
            label = { Text(stringResource(R.string.data_nav_button),
                color = MaterialTheme.colorScheme.surface) },
            selected = currentRoute == "page3",
            onClick = {
                navController.navigate("page3") {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.surface
            )
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Person, contentDescription = stringResource(R.string.icon_user)) },
            label = { Text(stringResource(R.string.user_nav_button),
                color = MaterialTheme.colorScheme.surface) },
            selected = currentRoute == "page4",
            onClick = {
                navController.navigate("page4") {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                unselectedIconColor = MaterialTheme.colorScheme.surface
            )
        )
    }
}

// First page for home screen
@SuppressLint("DefaultLocale")
@Composable
fun Page1() {

    var prices: List<Price> by remember { mutableStateOf(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            try {
                prices = fetchAndAdjustPrices()
                //prices = dummyPrices3
                loading = false
            } catch (e: Exception) {
                error = e.message
                loading = false
            }
        }
    }

    if (loading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = stringResource(R.string.loading_price_data_text))
        }
    } else if (error != null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Error: $error")
        }
    } else {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item{
                Text(
                    text = stringResource(R.string.cents_kwh_prices),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp)
                )
            }
            item{
                Spacer(modifier = Modifier.height(16.dp))
            }
            item{
                BarChart(prices = prices)
            }
            item{
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp)
                        .clip(RoundedCornerShape(6.dp)),
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        Text(
                            text = stringResource(R.string.date_title_price_column),
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        Text(
                            text = stringResource(R.string.time_title_price_column),
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                    ) {
                        Text(
                            text = stringResource(R.string.cents_kwh_title_price_column),
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth(),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }


            items(prices) { price ->
                val zonedDateTime = ZonedDateTime.parse(price.startDate)
                val date = zonedDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                val time = zonedDateTime.format(DateTimeFormatter.ofPattern("HH.mm"))
                val priceInCents = String.format("%.3f", price.price)

                // Check if this price is for the current hour
                val currentTime = ZonedDateTime.now()
                val isCurrentHour = zonedDateTime.hour == currentTime.hour &&
                        zonedDateTime.dayOfYear == currentTime.dayOfYear &&
                        zonedDateTime.year == currentTime.year

                // Color based on price range (same logic as bar chart)
                val priceColor = when {
                    price.price < 7 -> errorContainerLight
                    price.price < 14 -> primaryContainerLight
                    else -> secondaryLight
                }

                // Background color with appropriate opacity and highlighting for current hour
                val backgroundColor = if (isCurrentHour) {
                    priceColor.copy(alpha = 0.7f) // Lighter version for current hour
                } else {
                    priceColor.copy(alpha = 0.5f) // Very light tint for non-current hours
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 4.dp, top = 2.dp, end = 4.dp, bottom = 2.dp)
                        .clip(RoundedCornerShape(6.dp))
                        //.background(backgroundColor)
                        .border(
                            width = if (isCurrentHour) 4.dp else 0.dp,
                            color = if (isCurrentHour) priceColor else Color.Transparent,
                            shape = RoundedCornerShape(6.dp)
                        )
                    ,
                    horizontalArrangement = Arrangement.spacedBy(1.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(backgroundColor)
                            //.padding(8.dp)
                    ) {
                        Text(
                            text = date,
                            fontWeight = if (isCurrentHour) FontWeight.ExtraBold else FontWeight.Normal,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth(),
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(backgroundColor)
                            //.padding(8.dp)
                    ) {
                        Text(
                            text = time,
                            fontWeight = if (isCurrentHour) FontWeight.ExtraBold else FontWeight.Normal,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth(),
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .background(backgroundColor)
                            //.padding(8.dp)
                    ) {
                        Text(
                            text = "$priceInCents",
                            fontWeight = if (isCurrentHour) FontWeight.ExtraBold else FontWeight.Normal,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .padding(4.dp)
                                .fillMaxWidth(),
                        )
                    }
                }
            }
        }
    }
}

// Fetches latest data, adjusts the time zone since the data is UTC+0
// If API is used with specific hour, then it's the corrected time zone
// That is not how it's fetched here
suspend fun fetchAndAdjustPrices(): List<Price> {
    val response = RetrofitInstance.api.getPrices()
    val timeOffsetAmount = getCurrentTimeOffset()

    return response.prices.map { price ->
        val startDateTime = ZonedDateTime.parse(price.startDate).plusHours(timeOffsetAmount.toLong())
        val endDateTime = ZonedDateTime.parse(price.endDate).plusHours(timeOffsetAmount.toLong())

        Price(
            price = price.price,
            startDate = startDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME),
            endDate = endDateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        )
    }
}

data class Price(
    val price: Double,
    val startDate: String,
    val endDate: String
)

data class PriceResponse(
    val prices: List<Price>
)

interface ApiService {
    @GET(LATEST_PRICES_ENDPOINT)
    suspend fun getPrices(): PriceResponse
}

object RetrofitInstance {
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

fun getCurrentTimeOffset(): Int {
    val now = ZonedDateTime.now(ZoneId.of("Europe/Helsinki"))
    return now.offset.totalSeconds / 3600
}

@SuppressLint("DefaultLocale")
@Composable
fun BarChart(prices: List<Price>) {
    val currentTime = ZonedDateTime.now()
    val startTime = currentTime.minusHours(4)
    val endTime = startTime.plusHours(12)
    val filteredPrices = prices.filter {
        val priceTime = ZonedDateTime.parse(it.startDate)
        priceTime.isAfter(startTime) && priceTime.isBefore(endTime)
    }.sortedBy { ZonedDateTime.parse(it.startDate) }
    val maxPrice = filteredPrices.maxOfOrNull { it.price } ?: 0.0
    val minPrice = filteredPrices.minOfOrNull { it.price } ?: 0.0
    val hasNegativeValues = minPrice < 0

    // Adjust minYValue to 0 if all values are positive
    val minYValue = if (hasNegativeValues) minPrice else 0.0
    val yRange = (maxPrice - minYValue).coerceAtLeast(0.1) // Ensure non-zero range

    var selectedPrice by remember { mutableStateOf<Triple<Double, String, String>?>(null) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    val currentTimeFormatted = currentTime.format(DateTimeFormatter.ofPattern("HH:mm dd.MM.yyyy"))
    val currentPrice = filteredPrices.find { ZonedDateTime.parse(it.startDate).hour == currentTime.hour }?.price ?: 0.0

    Canvas(modifier = Modifier
        .padding(start = 40.dp, top = 16.dp, end = 16.dp, bottom = 32.dp)
        .fillMaxWidth()
        .height(200.dp) // Fixed height for the chart
        .pointerInput(Unit) {
            detectTapGestures { offset ->
                val gap = 4.dp.toPx()
                val barWidth = (size.width - gap * (filteredPrices.size - 1)) / filteredPrices.size
                val index = (offset.x / (barWidth + gap)).toInt()
                if (index in filteredPrices.indices) {
                    val price = filteredPrices[index]
                    val dateTime = ZonedDateTime.parse(price.startDate)
                    val time = dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                    val date = dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                    selectedPrice = Triple(price.price, time, date)
                    selectedIndex = index
                }
            }
        }
    ) {
        val gap = 6.dp.toPx()
        val barWidth = (size.width - gap * (filteredPrices.size - 1)) / filteredPrices.size
        val cornerRadius = 3.dp.toPx()

        // Calculate zero Y position
        val zeroY = if (hasNegativeValues) {
            // Position zero line proportionally between min and max
            size.height * (maxPrice / yRange).toFloat()
        } else {
            size.height // If no negative values, zero line at bottom
        }

        // Draw 8 evenly spaced horizontal grid lines
        val numLines = 8
        val yStep = yRange / (numLines - 1)

        for (i in 0 until numLines) {
            val yValue = minYValue + (i * yStep)
            val y = size.height - ((yValue - minYValue) / yRange * size.height).toFloat()

            drawLine(
                color = Color.Gray,
                start = androidx.compose.ui.geometry.Offset(0f, y),
                end = androidx.compose.ui.geometry.Offset(size.width, y),
                strokeWidth = 1.dp.toPx()
            )

            // Format Y-axis labels based on value size
            val label = when {
                abs(yValue) < 0.01 -> "0"
                abs(yValue) >= 10 -> String.format("%.0f", yValue)
                else -> String.format("%.1f", yValue)
            }

            drawContext.canvas.nativeCanvas.drawText(
                label,
                -32f,
                y + 4.sp.toPx(),
                android.graphics.Paint().apply {
                    color = android.graphics.Color.BLACK
                    textAlign = android.graphics.Paint.Align.RIGHT
                    textSize = 10.sp.toPx()
                }
            )
        }

        // Draw zero line more prominently if we have negative values
        if (hasNegativeValues) {
            drawLine(
                color = Color.Black,
                start = androidx.compose.ui.geometry.Offset(0f, zeroY),
                end = androidx.compose.ui.geometry.Offset(size.width, zeroY),
                strokeWidth = 1.5.dp.toPx()
            )
        }

        // Draw the bars
        filteredPrices.forEachIndexed { index, price ->
            val barHeightPercentage = abs(price.price / yRange).toFloat()
            val barHeight = barHeightPercentage * size.height
            val xOffset = index * (barWidth + gap)

            // For positive values, start from zeroY and go up (negative y direction)
            // For negative values, start from zeroY and go down (positive y direction)
            // Ensure the bars start exactly at the zero line
            val topLeft = if (price.price >= 0) {
                // For positive values, start exactly from zeroY
                androidx.compose.ui.geometry.Offset(xOffset, zeroY - barHeight)
            } else {
                // For negative values, start exactly from zeroY
                androidx.compose.ui.geometry.Offset(xOffset, zeroY)
            }

            // Ensure minimum bar height for visibility when values are very close to zero
            val minBarHeight = 1f.dp.toPx()
            val actualBarHeight = barHeight.coerceAtLeast(minBarHeight)

            val barColor = when {
                price.price < 7 -> errorContainerLight
                price.price < 14 -> primaryContainerLight
                else -> secondaryLight
            }

            // Change color opacity for current hour
            val priceTime = ZonedDateTime.parse(price.startDate)
//            if (priceTime.hour == currentTime.hour) {
//                barColor = barColor.copy(alpha = 0.6f)
//            }

            // Draw the bar
            drawRoundRect(
                color = barColor.copy(alpha = 0.9f),
                topLeft = topLeft,
                size = androidx.compose.ui.geometry.Size(
                    barWidth,
                    // Fix: For negative values, bars go down from zero line
                    if (price.price >= 0) actualBarHeight else actualBarHeight
                ),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius, cornerRadius)
            )

//            // Draw border for current hour
//            if (priceTime.hour == currentTime.hour) {
//                drawRoundRect(
//                    color = Color.Black,
//                    topLeft = topLeft,
//                    size = androidx.compose.ui.geometry.Size(barWidth, actualBarHeight),
//                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(cornerRadius, cornerRadius),
//                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
//                )
//            }

            // Draw x-axis labels
            val isCurrentHour = priceTime.hour == currentTime.hour
            val hourText = ZonedDateTime.parse(price.startDate).format(DateTimeFormatter.ofPattern("HH"))
            val isSelectedHour = selectedIndex == index
            val textPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.BLACK
                textAlign = android.graphics.Paint.Align.CENTER
                textSize = 12.sp.toPx()
                if (isSelectedHour) {
                    isFakeBoldText = true // Make text bold for current hour
                }
            }

            // Draw indicator line for selected hour label
            if (isSelectedHour) {
                // Draw vertical indicator line for current hour
                drawLine(
                    color = scrimLight.copy(alpha = 0.5f),
                    start = androidx.compose.ui.geometry.Offset(xOffset + barWidth / 2, 0f),
                    end = androidx.compose.ui.geometry.Offset(xOffset + barWidth / 2, size.height),
                    strokeWidth = 1.5.dp.toPx()
                )

//                // Draw background for current hour label
//                val textWidth = textPaint.measureText("00") // Approximate width
//                val labelBgRect = android.graphics.RectF(
//                    xOffset + barWidth / 2 - textWidth / 2 - 4.dp.toPx(),
//                    size.height + 2.sp.toPx(),
//                    xOffset + barWidth / 2 + textWidth / 2 + 4.dp.toPx(),
//                    size.height + 22.sp.toPx()
//                )
//                // Convert the Compose Color to Android Color with same alpha
//                val androidColor = android.graphics.Color.argb(
//                    (barColor.alpha * 0.5f * 255).toInt(),
//                    (barColor.red * 255).toInt(),
//                    (barColor.green * 255).toInt(),
//                    (barColor.blue * 255).toInt()
//                )
//
//                drawContext.canvas.nativeCanvas.drawRoundRect(
//                    labelBgRect,
//                    4.dp.toPx(), 4.dp.toPx(),
//                    android.graphics.Paint().apply {
//                        color = androidColor
//                        style = android.graphics.Paint.Style.FILL
//                    }
//                )
            }

            // Draw indicators for current hour label
            if (isCurrentHour) {
//                // Draw vertical indicator line for current hour
//                drawLine(
//                    color = barColor.copy(alpha = 0.5f),
//                    start = androidx.compose.ui.geometry.Offset(xOffset + barWidth / 2, 0f),
//                    end = androidx.compose.ui.geometry.Offset(xOffset + barWidth / 2, size.height),
//                    strokeWidth = 1.5.dp.toPx()
//                )

                // Draw background for current hour label
                val textWidth = textPaint.measureText("00") // Approximate width
                val labelBgRect = android.graphics.RectF(
                    xOffset + barWidth / 2 - textWidth / 2 - 4.dp.toPx(),
                    size.height + 2.sp.toPx(),
                    xOffset + barWidth / 2 + textWidth / 2 + 4.dp.toPx(),
                    size.height + 22.sp.toPx()
                )
                // Convert the Compose Color to Android Color with same alpha
                val androidColor = android.graphics.Color.argb(
                    (barColor.alpha * 0.5f * 255).toInt(),
                    (barColor.red * 255).toInt(),
                    (barColor.green * 255).toInt(),
                    (barColor.blue * 255).toInt()
                )

                drawContext.canvas.nativeCanvas.drawRoundRect(
                    labelBgRect,
                    4.dp.toPx(), 4.dp.toPx(),
                    android.graphics.Paint().apply {
                        color = androidColor
                        style = android.graphics.Paint.Style.FILL
                    }
                )
            }

            // Draw the hour label
            //val hourText = ZonedDateTime.parse(price.startDate).format(DateTimeFormatter.ofPattern("HH"))
            drawContext.canvas.nativeCanvas.drawText(
                hourText,
                xOffset + barWidth / 2,
                size.height + 16.sp.toPx(),
                textPaint.apply {
//                    if (isCurrentHour) {
//                        color = android.graphics.Color.WHITE
//                    }
                }
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Box(
        modifier = Modifier
            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = selectedPrice?.let { (price, time, date) ->
                stringResource(R.string.selected_spot_cents_kwh, time, date, String.format("%.2f", price))
            } ?: stringResource(R.string.current_spot_cents_kwh, currentTimeFormatted, String.format("%.2f", currentPrice)),
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
    }
}

// Second page for calculation of electricity bill
class Page2ViewModel : ViewModel() {
    var consumption by mutableStateOf("")
    var fixedPrice by mutableStateOf("")
    var yearlyCost by mutableStateOf<Double?>(null)
    var monthlyCost by mutableStateOf<Double?>(null)
    var roundedYearlyCost by mutableStateOf("")
    var roundedMonthlyCost by mutableStateOf("")
    var averageYearlyCost by mutableStateOf<Double?>(null)
    var averageMonthlyCost by mutableStateOf<Double?>(null)
    var roundedAverageYearlyCost by mutableStateOf("")
    var roundedAverageMonthlyCost by mutableStateOf("")
    var prices: List<Price> by mutableStateOf(emptyList())
    var loading by mutableStateOf(true)
    var error by mutableStateOf<String?>(null)

    init {
        fetchPrices()
    }

    private fun fetchPrices() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getPrices()
                prices = response.prices
                loading = false
            } catch (e: Exception) {
                error = e.message
                loading = false
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun Page2( viewModel: Page2ViewModel = viewModel()) {
    val keyboardController = LocalSoftwareKeyboardController.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            // Input fields
            OutlinedTextField(
                value = viewModel.consumption,
                onValueChange = { viewModel.consumption = it },
                label = { Text(stringResource(R.string.energy_consumption_kwh_year)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = viewModel.fixedPrice,
                onValueChange = { viewModel.fixedPrice = it },
                label = { Text(stringResource(R.string.fixed_price_cents_kwh)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = { keyboardController?.hide() }
                ),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            // Calculate costs
            val consumptionValue = viewModel.consumption.toDoubleOrNull()
            val fixedPriceValue = viewModel.fixedPrice.toDoubleOrNull()

            if (consumptionValue != null && fixedPriceValue != null) {
                viewModel.yearlyCost = consumptionValue * fixedPriceValue / 100
                viewModel.monthlyCost = viewModel.yearlyCost!! / 12

                viewModel.roundedYearlyCost = String.format("%.2f", viewModel.yearlyCost)
                viewModel.roundedMonthlyCost = String.format("%.2f", viewModel.monthlyCost)
            } else {
                viewModel.yearlyCost = null
                viewModel.monthlyCost = null
            }

            // Display calculated costs
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.fixed_price_title),
                    style = MaterialTheme.typography.titleLarge.copy(textDecoration = TextDecoration.Underline),
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = buildAnnotatedString {
                        if (viewModel.consumption.isEmpty() || viewModel.fixedPrice.isEmpty()) {
                            append(stringResource(R.string.input_both_values_to_calculate))
                        } else if (viewModel.yearlyCost == null || viewModel.monthlyCost == null) {
                            append(stringResource(R.string.invalid_input_values))
                        } else {
                            append(stringResource(R.string.yearly_cost))
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("${viewModel.roundedYearlyCost} €")
                            }
                            append(stringResource(R.string.monthly_cost))
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("${viewModel.roundedMonthlyCost} €")
                            }
                        }
                    },
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            // Display fetched data status and average price calculation
            if (viewModel.loading) {
                Text(text = stringResource(R.string.loading_price_data), textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            } else if (viewModel.error != null) {
                Text(text = "Error: ${viewModel.error}", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            } else {
                val averagePrice = viewModel.prices.map { it.price }.average()
                val roundedAveragePrice = String.format("%.2f", averagePrice)

                if (viewModel.consumption.isNotEmpty()) {
                    viewModel.averageYearlyCost = viewModel.consumption.toDoubleOrNull()?.let { it * averagePrice / 100 }
                    viewModel.averageMonthlyCost = viewModel.averageYearlyCost?.div(12)

                    viewModel.roundedAverageYearlyCost = viewModel.averageYearlyCost?.let { String.format("%.2f", it) } ?: ""
                    viewModel.roundedAverageMonthlyCost = viewModel.averageMonthlyCost?.let { String.format("%.2f", it) } ?: ""
                }
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.errorContainer)
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.recent_spot_price),
                        style = MaterialTheme.typography.titleLarge.copy(textDecoration = TextDecoration.Underline),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = buildAnnotatedString {
                            append(stringResource(R.string.average_price_from_past_few_days))
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                append("$roundedAveragePrice ")
                            }
                            append(stringResource(R.string.cents_kwh))
                            if (viewModel.consumption.isNotEmpty()) {
                                append(stringResource(R.string.average_yearly_cost))
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("${viewModel.roundedAverageYearlyCost} €")
                                }
                                append(stringResource(R.string.average_monthly_cost))
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("${viewModel.roundedAverageMonthlyCost} €")
                                }
                            } else {
                                append(stringResource(R.string.input_your_consumption_to_calculate_average_costs))
                            }
                        },
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        }
    }
}


@Composable
fun Page3() {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.padding(30.dp))
            Text(
                text = stringResource(R.string.wattsapp_uses_data_from_porssisahko_api_wich_provides_electricity_prices_in_finland),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = {
                val intentBrowserMain = Intent(Intent.ACTION_VIEW, Uri.parse(API_MAIN_PAGE_URL))
                context.startActivity(intentBrowserMain)
            },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(text = stringResource(R.string.api_main_page_button))
            }
            Spacer(modifier = Modifier.padding(25.dp))
            Text(
                text = stringResource(R.string.the_latest_prices_are_available_in_json_format_tomorrows_prices_are_available_after_14_15),
                textAlign = TextAlign.Center,
                fontSize = 20.sp,
                modifier = Modifier.padding(16.dp)
            )
            Button(
                onClick = {
                    val intentBrowserDataJSON = Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL + LATEST_PRICES_ENDPOINT))
                    context.startActivity(intentBrowserDataJSON)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            ) {
                Text(text = stringResource(R.string.data_in_json_button))
            }
        }
    }
}

// Fourth page for adding user name
@Composable
fun Page4(userName: String, onUserNameChange: (String) -> Unit) {
    var localUserName by remember { mutableStateOf(userName) }
    var errorMessage by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    var tempCameraUri by remember { mutableStateOf<Uri?>(null) }

    val cameraPermissionGranted = remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    var recomposeTrigger by remember { mutableStateOf(false) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            tempCameraUri?.let { uri ->
                imageUri = uri  // Only set imageUri on success
                sharedPreferences.edit().putString("image_uri", uri.toString()).apply()
            }
        }
        // Clear temp URI whether successful or not
        tempCameraUri = null
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        cameraPermissionGranted.value = isGranted
    }

    LaunchedEffect(userName) {
        localUserName = userName
    }

    LaunchedEffect(Unit, recomposeTrigger) {
        if (!cameraPermissionGranted.value) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
        val savedUri = sharedPreferences.getString("image_uri", null)
        if (savedUri != null) {
            imageUri = Uri.parse(savedUri)
        }
    }

    LaunchedEffect(imageUri) {
        imageUri?.let { uri ->
            sharedPreferences.edit().putString("image_uri", uri.toString()).apply()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.padding(20.dp))
            if (userName.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.not, userName),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp)
                )
                Button(onClick = {
                    onUserNameChange("")
                }) {
                    Text(stringResource(R.string.delete_user_name))
                }
                Spacer(modifier = Modifier.padding(20.dp))
//                Text(
//                    text = stringResource(R.string.or_change_the_user_name),
//                    fontSize = 24.sp,
//                    modifier = Modifier.padding(16.dp)
//                )
//                TextField(
//                    value = localUserName,
//                    onValueChange = { newValue ->
//                        if (newValue.length <= 16) {
//                            localUserName = newValue
//                            errorMessage = ""
//                        } else {
//                            errorMessage = "Username cannot exceed 16 characters"
//                        }
//                    },
//                    label = { Text(stringResource(R.string.name)) }
//                )
//                if (errorMessage.isNotEmpty()) {
//                    Text(
//                        text = errorMessage,
//                        color = Color.Red,
//                        modifier = Modifier.padding(8.dp)
//                    )
//                }
            } else {
                Text(
                    text = stringResource(R.string.who_s_using_this_app),
                    fontSize = 24.sp,
                    modifier = Modifier.padding(16.dp)
                )
                val keyboardController = LocalSoftwareKeyboardController.current
                OutlinedTextField(
                    value = localUserName,
                    onValueChange = { newValue ->
                        val filteredValue = newValue.replace("\n", "")
                        if (filteredValue.length <= 16) {
                            localUserName = filteredValue
                            errorMessage = ""
                        } else {
                            errorMessage = "Username cannot exceed 16 characters"
                        }
                    },
                    label = { Text(stringResource(R.string.textfield_label_name_2)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Button(onClick = {
                    if (localUserName.length <= 16) {
                        onUserNameChange(localUserName)
                        localUserName = "" // Clear the input field
                    }
                }) {
                    Text(stringResource(R.string.save_button))
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                imageUri = null  // Reset imageUri to prevent flashing the old image
                val photoFile = File(context.filesDir, "photo.jpg")
                val photoUri = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    photoFile
                )
                tempCameraUri = photoUri  // Store URI temporarily
                cameraLauncher.launch(photoUri)
            }) {
                Text(stringResource(R.string.take_pic_button))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        item {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surface)
                    .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
            ) {
                imageUri?.let { uri ->
                    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .rotate(90f),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }

//        item {
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(onClick = {
//                recomposeTrigger = !recomposeTrigger
//            }) {
//                Text(stringResource(R.string.update_pic_button))
//            }
//        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                imageUri = null
                sharedPreferences.edit().remove("image_uri").apply()
            }) {
                Text(stringResource(R.string.delete_pic_button))
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}