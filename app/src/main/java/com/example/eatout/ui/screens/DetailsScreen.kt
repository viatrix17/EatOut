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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import com.example.eatout.ui.components.CustomAlertDialog
import com.example.eatout.ui.components.DishCard
import com.example.eatout.ui.components.DishesList
import com.example.eatout.ui.components.TagLabel
import com.example.eatout.viewmodel.Dish
import androidx.compose.foundation.lazy.items

@Composable
fun DetailsScreen(
    restaurantId: Int,
    restaurantViewModel: RestaurantViewModel = viewModel(),
    dishViewModel: DishViewModel = viewModel()
){
    var showAlreadyAddedRestaurantDialog by remember { mutableStateOf(false) }

    if (showAlreadyAddedRestaurantDialog) {
        CustomAlertDialog(
            onDismissRequest = { showAlreadyAddedRestaurantDialog = false },
            title = "Restaurant Added",
            message = "This restaurant is already on your To Visit List"
        )

    }

    var showAlreadyAddedDishDialog by remember { mutableStateOf(false) }

    if (showAlreadyAddedDishDialog) {
        CustomAlertDialog(
            onDismissRequest = { showAlreadyAddedDishDialog = false },
            title = "Dish Added",
            message = "This dish is already on your To Try List"
        )
    }

    val listState = rememberLazyListState()

    val currentRestaurant = restaurantViewModel.getRestaurantById(restaurantId)

    if (currentRestaurant != null) {
        val dishes = dishViewModel.getDishesForRestaurant(restaurantId)

        DetailsPhoneLayout(
            restaurantViewModel = restaurantViewModel,
            dishViewModel = dishViewModel,
            restaurant = currentRestaurant,
            dishes = dishes,
            onAddRestaurantClick = { restaurant ->
                if (restaurant.isToVisit) {
                    showAlreadyAddedRestaurantDialog = true
                } else {
                    restaurantViewModel.toggleToVisit(restaurant.id)
                }
            },
            onAddDishClick = { dish ->
                if (dish.isToTry) {
                    showAlreadyAddedDishDialog = true
                } else {
                    dishViewModel.toggleToTry(dish.id)
                }
            },
            listState = listState
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
    dishes: List<Dish>,
    onAddRestaurantClick: (Restaurant) -> Unit,
    onAddDishClick: (Dish) -> Unit,
    listState: LazyListState = rememberLazyListState(),
){
    LazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp),
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "${restaurant.name}",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column() {
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
                    Spacer(modifier = Modifier.weight(1f))

                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.End
                    ) {
                        Row {
                            IconButton(onClick = { onAddRestaurantClick(restaurant) }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Ulubione",
                                    modifier = Modifier.size(24.dp),
                                    tint = if (!restaurant.isToVisit) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }

                            IconButton(onClick = { restaurantViewModel.toggleFavourite(restaurant.id) }) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = "Ulubione",
                                    modifier = Modifier.size(24.dp),
                                    tint = if (restaurant.isFavorite) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                            }
                        }
                    }
                }

            }

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Menu",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
            )

        }

        items(dishes, key = { it.id }) { dish ->
            DishCard(
                dish = dish,
                onClick = {},
                viewModel = dishViewModel,
                onAddClick = { onAddDishClick(dish) }
            )
        }
    }
}