package com.example.eatout.ui.screens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController

@Composable
fun FavouriteScreen(
    isTablet: Boolean,
    navController: NavHostController
) {
    FavouritePhoneLayout()
}

@Composable
fun FavouritePhoneLayout()
{
    Text(
        text = "Saved dishes",
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface,
        textAlign = TextAlign.Center
    )
}