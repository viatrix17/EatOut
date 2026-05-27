package com.example.eatout.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DinnerDining
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.LunchDining
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopBar(
    title: String,
//    isDarkTheme: Boolean,
//    onToggleTheme: () -> Unit,
    onBackClick: () -> Unit,
    showBackButton: Boolean,
    modifier: Modifier = Modifier
) {

    CenterAlignedTopAppBar(
        modifier = modifier,
        title = {
            Text(text = title)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
        ),
        navigationIcon = {
            if (showBackButton) {
                ReturnButton(
                    onClick = onBackClick,
                    modifier = Modifier.padding(8.dp)
                )
            } else {
                IconButton(onClick = {  }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        },
        actions = {
            IconButton(onClick = {  }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Norifications",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}

@Composable
fun CustomBottomBar(
    navController: NavHostController
){
    BottomAppBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Spacer(Modifier.weight(1f))
        // favs
        IconButton(onClick = { navController.navigate("favourite") }) {
            Icon(Icons.Default.Star, contentDescription = "Menu")
        }
        Spacer(Modifier.weight(1f))
        // restaurants
        IconButton(onClick = { navController.navigate("to-visit") }) {
            Icon(Icons.Default.Restaurant, contentDescription = "restaurants to visit")
        }
        Spacer(Modifier.weight(1f))

        // dishes
        IconButton(onClick = { navController.navigate("dishes") }) {
            Icon(Icons.Default.LunchDining, contentDescription = "Saved dishes")
        }
        Spacer(Modifier.weight(1f))
    }

}
