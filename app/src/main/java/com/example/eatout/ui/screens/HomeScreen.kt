package com.example.eatout.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eatout.R

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
            .padding(16.dp)
    ) {
        Card(
            onClick = onBrowseClick,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
//            Image(
//                painter = painterResource(id = R.drawable.Browse),
//                contentDescription = "Opis obrazka dla dostępności",
//                modifier = Modifier.fillMaxWidth().height(150.dp),
//                contentScale = ContentScale.Crop // Przycina obraz, aby wypełnił przestrzeń
//            )
            Text(
                text = "Browse",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

        }
        Card(
            onClick = onClosestClick,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            //Image(
//                painter = painterResource(id = R.drawable.Browse),
//                contentDescription = "Opis obrazka dla dostępności",
//                modifier = Modifier.fillMaxWidth().height(150.dp),
//                contentScale = ContentScale.Crop // Przycina obraz, aby wypełnił przestrzeń
//            )
            Text(
                text = "Closest to You",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

        }
        Card(
            onClick = onRecommendationsClick,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {

          //  Image(
//                painter = painterResource(id = R.drawable.Browse),
//                contentDescription = "Opis obrazka dla dostępności",
//                modifier = Modifier.fillMaxWidth().height(150.dp),
//                contentScale = ContentScale.Crop // Przycina obraz, aby wypełnił przestrzeń
//            )
            Text(
                text = "Daily Recommendation",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

        }
    }

}

// landscape phone layout == tablet layout ig