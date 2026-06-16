    package com.example.eatout.ui.screens
    import android.util.Log
    import androidx.compose.foundation.Image
    import androidx.compose.foundation.layout.Arrangement
    import androidx.compose.foundation.layout.Column
    import androidx.compose.material3.Button
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Text
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.collectAsState
    import androidx.compose.runtime.getValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.res.painterResource
    import androidx.compose.ui.text.style.TextAlign
    import androidx.navigation.NavHostController
    import com.example.eatout.domain.model.RestaurantUIState
    import com.example.eatout.viewmodel.RestaurantViewModel
    import com.example.eatout.R
    @Composable
    fun RecommendationScreen(
        isTablet: Boolean,
        navController: NavHostController,
        viewModel: RestaurantViewModel
    ) {
        // Obserwujemy stan z ViewModelu
        val restaurant by viewModel.dailyRecommendation.collectAsState()

        if (restaurant == null) {
            // STAN 1: Brak wylosowanej restauracji -> Pokaż obrazek i przycisk
            RecommendationEmptyLayout(
                onGenerateClick = { viewModel.triggerDailyRecommendation() }
            )
        } else {
            // STAN 2: Jest restauracja -> Pokaż szczegóły
            RecommendationPhoneLayout(restaurant = restaurant!!)
        }
    }

    @Composable
    fun RecommendationEmptyLayout(onGenerateClick: () -> Unit) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Obrazek (zmień R.drawable.twoj_obrazek na swój zasób)
            Image(
                painter = painterResource(id = R.drawable.ic_placeholder),
                contentDescription = "Brak rekomendacji"
            )

            Button(onClick = onGenerateClick) {
                Text("Wylosuj restaurację na dziś!")
            }
        }
    }
    @Composable
    fun RecommendationPhoneLayout(
        restaurant: RestaurantUIState
    ) {
        Column {
            Text(
                text = restaurant.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(text = restaurant.address)
        }
    }