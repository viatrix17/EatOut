package com.example.eatout.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.eatout.ui.components.RestaurantsList
import com.example.eatout.ui.components.SimpleVerticalScrollbar
import com.example.eatout.viewmodel.RestaurantViewModel
import androidx.compose.runtime.getValue
import com.example.eatout.viewmodel.Restaurant

@Composable
fun BrowseScreen(
    viewModel: RestaurantViewModel = viewModel(),
    isTablet: Boolean,
    navController: NavHostController
) {
    val restaurants by viewModel.currentRestaurants.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.selectAll()
    }

    val listState = rememberLazyListState()

    BrowsePhoneLayout(
        listState = listState,
        data = restaurants
    )
}

@Composable
fun BrowsePhoneLayout(
    listState: LazyListState = rememberLazyListState(),
    data: List<Restaurant>
) {
    Box(modifier = Modifier.fillMaxSize()) {
        RestaurantsList(
            data = data,
            onRestaurantSelected = {},
            listState = listState
        )
        SimpleVerticalScrollbar(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(2.dp),
            listState = listState
        )
    }
}