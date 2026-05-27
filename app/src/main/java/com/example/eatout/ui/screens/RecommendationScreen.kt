package com.example.eatout.ui.screens
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController

@Composable
fun RecommendationScreen(
    isTablet: Boolean,
    navController: NavHostController
) {
    RecommendationPhoneLayout()
}

@Composable
fun RecommendationPhoneLayout()
{
    Text(
        text = "Recommendation",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center
    )
}