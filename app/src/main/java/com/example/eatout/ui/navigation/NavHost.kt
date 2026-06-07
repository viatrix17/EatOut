package com.example.eatout.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eatout.ui.screens.DishesScreen
import com.example.eatout.ui.screens.HomeScreen
import com.example.eatout.ui.screens.LocationSettingsScreen
import com.example.eatout.ui.screens.ModeSettingsScreen
import com.example.eatout.ui.screens.RecommendationScreen
import com.example.eatout.ui.screens.RestaurantListScreen
import com.example.eatout.viewmodel.MainViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
//    isDarkTheme: Boolean,
    isLoading: Boolean,
    isTablet: Boolean,
    modifier: Modifier = Modifier
) {
    val viewModel: MainViewModel = viewModel()
    Box(modifier = Modifier) {
        NavHost(
                navController = navController,
                startDestination = "home",
                modifier = modifier
            ) {
//        composable("welcome") {
//            WelcomeScreen(
//                onNavigateToHome = {
//                    navController.navigate("home") {
//                        popUpTo("welcome") { inclusive = true }
//                    }
//                },
//                isDarkTheme = isDarkTheme
//            )
//        }
                composable("home") {
                    HomeScreen(
                        isTablet = isTablet,
                        navController = navController
                    )
                }

                composable(route = "browse") {
                    RestaurantListScreen(
                        isTablet = isTablet,
                        navController = navController
                    )
                }
                composable(route = "closest") {
                    RestaurantListScreen(
                        isTablet = isTablet,
                        navController = navController,
                        showDistance = true
                    )
                }
                composable(route = "recommendation")
                {
                    RecommendationScreen(
                        isTablet = isTablet,
                        navController = navController
                    )
                }

                composable(route = "favourite")
                {
                    RestaurantListScreen(
                        isTablet = isTablet,
                        navController = navController,
                        listType = "FAVORITES"
                    )
                }

                composable(route = "to-visit")
                {
                    RestaurantListScreen(
                        isTablet = isTablet,
                        navController = navController,
                        listType = "TO_VISIT"
                    )
                }

                composable(route = "dishes")
                {
                    DishesScreen(
                        isTablet = isTablet,
                        navController = navController
                    )
                }
                composable(route = "location-settings")
                {
                    LocationSettingsScreen(
                        isTablet = isTablet,
                        viewModel = viewModel
                    )
                }
                composable(route = "mode-settings")
                {
                    ModeSettingsScreen(
                        isTablet = isTablet,
                        viewModel = viewModel
                    )
                }
            }
        if (isLoading) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
