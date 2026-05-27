package com.example.eatout.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eatout.ui.screens.BrowseScreen
import com.example.eatout.ui.screens.ClosestToYouScreen
import com.example.eatout.ui.screens.HomeScreen
import com.example.eatout.ui.screens.RecommendationScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
//    isDarkTheme: Boolean,
    isTablet: Boolean,
    modifier: Modifier = Modifier
) {
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

        composable(route="browse") {
            BrowseScreen(
                isTablet = isTablet,
                navController = navController
            )
        }
        composable(route="closest") {
            ClosestToYouScreen(
                isTablet = isTablet,
                navController = navController
            )
        }
        composable(route="recommendation")
        {
            RecommendationScreen(
                isTablet = isTablet,
                navController = navController
            )
        }
    }
}
