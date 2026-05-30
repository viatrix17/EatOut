package com.example.eatout.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.eatout.ui.NoteViewModel
import com.example.eatout.ui.screens.BrowseScreen
import com.example.eatout.ui.screens.ClosestToYouScreen
import com.example.eatout.ui.screens.DishesScreen
import com.example.eatout.ui.screens.FavouriteScreen
import com.example.eatout.ui.screens.HomeScreen
import com.example.eatout.ui.screens.RecommendationScreen
import com.example.eatout.ui.screens.ToVisitScreen

@Composable
fun AppNavHost(
    navController: NavHostController,
//    isDarkTheme: Boolean,
    isLoading: Boolean,
    isTablet: Boolean,
    modifier: Modifier = Modifier,
    noteViewModel : NoteViewModel
) {

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
                    BrowseScreen(
                        isTablet = isTablet,
                        navController = navController,
                        noteViewModel = noteViewModel
                    )
                }
                composable(route = "closest") {
                    ClosestToYouScreen(
                        isTablet = isTablet,
                        navController = navController
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
                    FavouriteScreen(
                        isTablet = isTablet,
                        navController = navController,
                        noteViewModel = noteViewModel
                    )
                }

                composable(route = "to-visit")
                {
                    ToVisitScreen(
                        isTablet = isTablet,
                        navController = navController
                    )
                }

                composable(route = "dishes")
                {
                    DishesScreen(
                        isTablet = isTablet,
                        navController = navController
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
