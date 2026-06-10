package com.example.eatout.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.eatout.data.repository.NoteRepository
import com.example.eatout.data.repository.LocationRepository
import com.example.eatout.data.repository.RestaurantRepository
import com.example.eatout.viewmodel.NoteViewModel
import com.example.eatout.ui.screens.DetailsScreen
import com.example.eatout.ui.screens.DishesScreen
import com.example.eatout.ui.screens.HomeScreen
import com.example.eatout.ui.screens.LocationSettingsScreen
import com.example.eatout.ui.screens.ModeSettingsScreen
import com.example.eatout.ui.screens.RecommendationScreen
import com.example.eatout.ui.screens.RestaurantListScreen
import com.example.eatout.util.LocalRepository
import com.example.eatout.util.RestaurantViewModelFactory
import com.example.eatout.viewmodel.DishViewModel
import com.example.eatout.viewmodel.MainViewModel
import com.example.eatout.viewmodel.RestaurantViewModel

@Composable
fun AppNavHost(
    navController: NavHostController,
    repository: RestaurantRepository,
    locationRepository: LocationRepository,
    noteRepository: NoteRepository,
    isLoading: Boolean,
    isTablet: Boolean,
    modifier: Modifier = Modifier
) {
    val viewModel: MainViewModel = viewModel()
    val noteViewModel: NoteViewModel = viewModel()

    val factory = RestaurantViewModelFactory(repository, locationRepository, noteRepository)
    val restaurantViewModel: RestaurantViewModel = viewModel(factory = factory)

    val dishViewModel: DishViewModel = viewModel()

    Box(modifier = Modifier) {
        CompositionLocalProvider(LocalRepository provides repository) {
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
                        navController = navController,
                        viewModel = restaurantViewModel,
                        locationRepository = locationRepository
                    )
                }
                composable(route = "closest") {
                    RestaurantListScreen(
                        isTablet = isTablet,
                        navController = navController,
                        showDistance = true,
                        viewModel = restaurantViewModel,
                        locationRepository = locationRepository
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
                        listType = "FAVORITES",
                        viewModel = restaurantViewModel,
                        locationRepository = locationRepository
                    )
                }

                composable(route = "to-visit")
                {
                    RestaurantListScreen(
                        isTablet = isTablet,
                        navController = navController,
                        listType = "TO_VISIT",
                        viewModel = restaurantViewModel,
                        locationRepository = locationRepository
                    )
                }

                composable(
                    route = "details/{restaurantId}",
                    arguments = listOf(navArgument("restaurantId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val restaurantId = backStackEntry.arguments?.getLong("restaurantId") ?: -1

                    DetailsScreen(
                        restaurantId = restaurantId,
                        restaurantViewModel = restaurantViewModel,
                        dishViewModel = dishViewModel
                    )
                }

                composable(route = "dishes")
                {
                    DishesScreen(
                        isTablet = isTablet,
                        navController = navController,
                        viewModel = dishViewModel
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
        }
        if (isLoading) {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}
