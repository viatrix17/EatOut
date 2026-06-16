package com.example.eatout.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.location.LocationManagerCompat.getCurrentLocation
import androidx.navigation.NavHostController
import com.example.eatout.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@Composable
fun HomeScreen(
    isTablet: Boolean,
    navController: NavHostController

) {
    HomePhoneLayout(
        onClosestClick = { navController.navigate("closest") },
        onBrowseClick = { navController.navigate("browse") },
        onRecommendationsClick = { navController.navigate("recommendation") }
    )
}


@Composable
fun HomePhoneLayout(
    onClosestClick: () -> Unit,
    onBrowseClick: () -> Unit,
    onRecommendationsClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        BigMenuCard(text = "Browse", icon = R.drawable.outline_food_bank_24, modifier = Modifier.weight(1f), onClick = onBrowseClick)
        BigMenuCard(text = "Closest to You", icon = R.drawable.outline_map_24, modifier = Modifier.weight(1f), onClick = onClosestClick)
        BigMenuCard(text = "Daily Recommendation", icon = R.drawable.outline_featured_seasonal_and_gifts_24, modifier = Modifier.weight(1f), onClick = onRecommendationsClick)
    }
}

@Composable
fun BigMenuCard(text: String, icon: Int, modifier: Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = icon),
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp),
                contentScale = ContentScale.Fit
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}