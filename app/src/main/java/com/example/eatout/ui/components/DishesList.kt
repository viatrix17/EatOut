package com.example.eatout.ui.components


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.items
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
import com.example.eatout.R
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow
import com.example.eatout.domain.model.Dish
import com.example.eatout.viewmodel.DishViewModel

@Composable
fun DishesList(
    data: List<Dish>,
    listState: LazyListState = rememberLazyListState(),
    viewModel: DishViewModel,
    modifier: Modifier = Modifier
) {

    var showAlreadyAddedDialog by remember { mutableStateOf(false) }

    var selectedDish by remember { mutableStateOf<Dish?>(null) }

    if (showAlreadyAddedDialog) {
        CustomAlertDialog(
            onDismissRequest = { showAlreadyAddedDialog = false },
            title = "Dish Added",
            message = "This dish is already on your To Try List"
        )
    }

    selectedDish?.let { dish ->
        CustomAlertDialog(
            onDismissRequest = { selectedDish = null },
            title = dish.name,
            message = "Ingredients: ${dish.ingredients}"
        )
    }

    LazyColumn(
        state = listState,
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(data, key = { it.id }) { dish ->
            DishCard(
                dish = dish,
                onClick = { selectedDish = dish },
                viewModel = viewModel,
                onAddClick = {
                    if (dish.isToTry) {
                        showAlreadyAddedDialog = true
                    } else {
                        viewModel.toggleToTry(dish.id)
                    }
                },
            )
        }
    }
}

@Composable
fun DishCard(
    dish: Dish,
    onClick: () -> Unit,
    viewModel: DishViewModel,
    onAddClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
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
                    .size(64.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = dish.name,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${dish.price} zł",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary, // Wyróżnienie ceny
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = dish.ingredients,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column {
                IconButton(onClick = onAddClick, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = if(dish.isToTry) Icons.Default.Remove else Icons.Default.Add,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                IconButton(onClick = { viewModel.toggleFavourite(dish.id) }, modifier = Modifier.size(36.dp)) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = if (dish.isFavorite) MaterialTheme.colorScheme.tertiary else MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}