package com.example.eatout.ui.screens
import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.eatout.viewmodel.RestaurantViewModel

@Composable
fun RecommendationScreen(
    isTablet: Boolean,
    navController: NavHostController,
    viewModel: RestaurantViewModel
) {
    RecommendationPhoneLayout(viewModel)
}

@Composable
fun RecommendationPhoneLayout(viewModel: RestaurantViewModel)
{
    Text(
        text = viewModel.getRecomendedRestaurant().toString(),
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center
    )
}