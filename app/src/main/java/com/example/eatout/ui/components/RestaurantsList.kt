package com.example.eatout.ui.components

import android.R.attr.data
import android.R.attr.maxLines
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.eatout.domain.model.Restaurant
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TextButton
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eatout.R
import com.example.eatout.viewmodel.RestaurantViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import com.example.eatout.domain.model.RestaurantUIState

@Composable
fun RestaurantsList(
    data: List<Pair<RestaurantUIState, Double>>,
    onRestaurantSelected: (RestaurantUIState) -> Unit,
    listState: LazyListState = rememberLazyListState(),
    viewModel: RestaurantViewModel,
    showDistance: Boolean = false,
    isToVisitScreen: Boolean
){
    var showAlreadyAddedDialog by remember { mutableStateOf(false) }


    if (showAlreadyAddedDialog) {
        CustomAlertDialog(
            onDismissRequest = { showAlreadyAddedDialog = false },
            title = "Restaurant Added",
            message = "This restaurant is already on your To Visit List"
        )

    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(data, key = { it.first.id }) { (restaurant, distance) ->
            RestaurantCard(
                restaurant = restaurant,
                onClick = { onRestaurantSelected(restaurant) },
                onAddClick = {
                    if (restaurant.isToVisit && !isToVisitScreen) {
                        showAlreadyAddedDialog = true
                    } else if (!isToVisitScreen){
                        viewModel.addToVisit(restaurant)
                    }
                    else {
                        viewModel.removeFromToVisit(restaurant)
                    }
                },
                onFavoriteAddClick = { viewModel.toggleFavourite(restaurant) },
                distance = distance,
                showDistance = showDistance,
                isToVisitScreen = isToVisitScreen
            )
        }
    }
}

@Composable
fun RestaurantCard(
    restaurant: RestaurantUIState,
    onClick: () -> Unit,
    onFavoriteAddClick: () -> Unit,
    onAddClick: () -> Unit,
    distance: Double,
    showDistance: Boolean,
    isToVisitScreen: Boolean
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_placeholder),
                contentDescription = null,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 12.dp)
            ) {
                Text(
                    text = restaurant.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = restaurant.address,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = restaurant.cuisineType,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))
                @OptIn(ExperimentalLayoutApi::class)
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    restaurant.tags.forEach { tag ->
                        TagLabel(text = tag, isCompact = true)
                    }
                }
            }
            Column(horizontalAlignment = Alignment.End) {
                if (showDistance) {
                    Text(
                        text = "%.1f km".format(distance),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
                Row {
                    IconButton(onClick = onAddClick, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = if (!isToVisitScreen) Icons.Default.Add else Icons.Default.Remove,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onFavoriteAddClick, modifier = Modifier.size(36.dp)) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (restaurant.isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}