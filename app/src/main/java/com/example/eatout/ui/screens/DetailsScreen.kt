package com.example.eatout.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
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
import com.example.eatout.domain.model.Restaurant
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
import com.example.eatout.domain.model.Dish
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import com.example.eatout.data.repository.RestaurantRepository
import com.example.eatout.domain.model.RestaurantUIState
import com.example.eatout.util.LocalRepository
import com.example.eatout.util.RestaurantViewModelFactory

@Composable
fun DetailsScreen(
    restaurantId: Long,
    restaurantViewModel: RestaurantViewModel,
    dishViewModel: DishViewModel,
    isDarkTheme: Boolean
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



    var selectedDish by remember { mutableStateOf<Dish?>(null) }

    selectedDish?.let { dish ->
        CustomAlertDialog(
            onDismissRequest = { selectedDish = null },
            title = dish.name,
            message = "Ingredients: ${dish.ingredients}"
        )
    }

    val listState = rememberLazyListState()

    val currentRestaurant = restaurantViewModel.getRestaurantById(restaurantId)

    val restaurantState by remember(restaurantId) {
        restaurantViewModel.getRestaurantByIdFlow(restaurantId)
    }.collectAsState()

    val dishes by dishViewModel.filteredDishes.collectAsState()
    LaunchedEffect(Unit) {
        dishViewModel.selectAll()
    }

    if (restaurantState != null) {
        DetailsPhoneLayout(
            restaurantViewModel = restaurantViewModel,
            dishViewModel = dishViewModel,
            restaurant = restaurantState!!,
            dishes = dishes,
            onAddRestaurantClick = { restaurant ->
                if (restaurant.isToVisit) {
                    showAlreadyAddedRestaurantDialog = true
                } else {
                    restaurantViewModel.addToVisit(restaurant)
                }
            },
            onAddDishClick = { dish ->
                if (dish.isToTry) {
                    showAlreadyAddedDishDialog = true
                } else {
                    dishViewModel.toggleToTry(dish.id)
                }
            },
            listState = listState,
            onClick = { dish ->
                selectedDish = dish },
            isDarkTheme = isDarkTheme
        )
    } else {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Restauration not found: $restaurantId")
        }
    }
}

@Composable
fun DetailsPhoneLayout(
    restaurantViewModel: RestaurantViewModel,
    dishViewModel: DishViewModel,
    restaurant: RestaurantUIState,
    dishes: List<Dish>,
    onAddRestaurantClick: (RestaurantUIState) -> Unit,
    onAddDishClick: (Dish) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    onClick: (Dish) -> Unit,
    isDarkTheme: Boolean
) {
    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Image(
                painter = painterResource(id = R.drawable.ic_placeholder),
                contentDescription = null,
                colorFilter = ColorFilter.tint(if (isDarkTheme) Color.White else Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp), // Nieco wyższy dla lepszego efektu
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = restaurant.name,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = restaurant.address,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Row {
                        IconButton(onClick = { onAddRestaurantClick(restaurant) }) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add to visit",
                                tint = if (restaurant.isToVisit) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                            )
                        }
                        IconButton(onClick = { restaurantViewModel.toggleFavourite(restaurant) }) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Favorite",
                                tint = if (restaurant.isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    restaurant.tags.forEach { tag -> TagLabel(text = tag) }
                }
            }

            Text(
                text = "Menu",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )
            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
        }

        items(dishes, key = { it.id }) { dish ->
            Box(modifier = Modifier.padding(horizontal = 8.dp)) {
                DishCard(
                    dish = dish,
                    onClick = { onClick(dish) },
                    viewModel = dishViewModel,
                    onAddClick = { onAddDishClick(dish) }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}