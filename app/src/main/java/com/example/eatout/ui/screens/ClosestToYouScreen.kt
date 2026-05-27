package com.example.eatout.ui.screens
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController

@Composable
fun ClosestToYouScreen(
    isTablet: Boolean,
    navController: NavHostController
) {
    ClosestPhoneLayout()
}

@Composable
fun ClosestPhoneLayout()
{
    Text(
        text = "Closest To You",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center
    )
}