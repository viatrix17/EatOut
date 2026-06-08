package com.example.eatout.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eatout.R
import com.example.eatout.viewmodel.DishViewModel
import com.example.eatout.viewmodel.Restaurant
import com.example.eatout.viewmodel.RestaurantViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.eatout.ui.components.TagLabel

@Composable
fun DetailsScreen(
    restaurantId: Int,
    restaurantViewModel: RestaurantViewModel = viewModel(),
    dishViewModel: DishViewModel = viewModel()
){
    val restaurant = restaurantViewModel.getRestaurantById(restaurantId)

    if (restaurant != null) {
        DetailsPhoneLayout(
            restaurantViewModel = restaurantViewModel,
            dishViewModel = dishViewModel,
            restaurant = restaurant
        )
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Nie znaleziono restauracji o ID: $restaurantId")
            Text("Sprawdź, czy lista w ViewModelu nie jest pusta.")
        }
    }
}

@Composable
fun DetailsPhoneLayout(
    restaurantViewModel: RestaurantViewModel,
    dishViewModel: DishViewModel,
    restaurant: Restaurant,
//    onAddRestaurantClick: () -> Unit,
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ){
        Column(
            modifier = Modifier
                .weight(2f)
                .padding(horizontal = 12.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_placeholder), // TO DO dodać biały ic_placeholder
                contentDescription = "Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "${restaurant.name}",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                "${restaurant.location}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))


            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                restaurant.tags.forEach { tag ->
                    TagLabel(text = tag)
                }
            }
        }
        Column( modifier = Modifier
            .weight(1f)
            .padding(horizontal = 12.dp)
        ) {
//            IconButton(onClick = onAddClick) {
//                Icon(
//                    imageVector = Icons.Default.Add,
//                    contentDescription = "Ulubione",
//                    modifier = Modifier.size(24.dp),
//                    tint = if (!restaurant.isToVisit) MaterialTheme.colorScheme.primary
//                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
//                )
//            }
//
//            IconButton(onClick = { viewModel.toggleFavourite(restaurant.id) }) {
//                Icon(
//                    imageVector = Icons.Default.Star,
//                    contentDescription = "Ulubione",
//                    modifier = Modifier.size(24.dp),
//                    tint = if (restaurant.isFavorite) MaterialTheme.colorScheme.primary
//                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
//                )
//            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
    }
}