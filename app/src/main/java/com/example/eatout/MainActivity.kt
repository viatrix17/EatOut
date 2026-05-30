package com.example.eatout

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import com.example.eatout.network.ApiService
import com.example.eatout.ui.components.CustomTopBar
import com.example.eatout.ui.theme.EatOutTheme
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.eatout.data.Note
import com.example.eatout.ui.NoteViewModel
import com.example.eatout.ui.components.CustomBottomBar
import com.example.eatout.ui.navigation.AppNavHost
import com.example.eatout.viewmodel.MainViewModel

class GlobalData {
    companion object {
        var restaurants = "error"
        var Flag = false
        var ListOfRestaurants: ArrayList<String> = arrayListOf()
        var ListOfFavourites: ArrayList<Note> = arrayListOf()
        var ListOfRestaurantsToVisit: ArrayList<Note> = arrayListOf()

        var ListOfDishes: ArrayList<Note> = arrayListOf()

        var ListOfFavouriteDishes: ArrayList<Note> = arrayListOf()


    }
}

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @SuppressLint("ViewModelConstructorInComposable")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EatOutTheme {
                val topBarTitle = "EatOut"
//                val configuration = LocalConfiguration.current
                val isTablet = false// configuration.smallestScreenWidthDp >= 600

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                val isLoading by viewModel.isLoading.collectAsState()


                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = {
                            val isHome = currentDestination?.route == "home"
                            val isNotReady = currentDestination == null
                            CustomTopBar(
                                title = topBarTitle,
                                onBackClick = {
                                    navController.popBackStack()
                                },
                                showBackButton = !isHome && !isNotReady,
                                modifier = Modifier
                            )
                        },
                        bottomBar = {
                            CustomBottomBar(
                                navController = navController
                            )
                        }
                    ) { innerPadding ->
                        AppNavHost(
                            navController = navController,
                            isLoading = isLoading,
                            isTablet = isTablet,
                            noteViewModel = NoteViewModel(),
                            modifier = Modifier.padding(innerPadding)

                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(textToShow: String, modifier: Modifier = Modifier) {
    Text(
        text = textToShow,
        modifier = modifier
    )
}