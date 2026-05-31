package com.example.eatout.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import com.example.eatout.ui.components.FilterButton
import com.example.eatout.viewmodel.Restaurant
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.eatout.ui.components.FilterBottomSheet
import com.example.eatout.viewmodel.ListType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouriteScreen(
    viewModel: RestaurantViewModel = viewModel(),
    isTablet: Boolean,
    navController: NavHostController
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val restaurants by viewModel.filteredRestaurants.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.selectAll()
    }

    LaunchedEffect(Unit) {
        viewModel.setListType(ListType.FAVORITES)
    }

    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var showBottomSheet by remember { mutableStateOf(false) }

    FavouritePhoneLayout(
        listState = listState,
        data = restaurants,
        searchQuery = searchQuery,
        onSearchQueryChange = { viewModel.onSearchQueryChange(it)},
        keyboardController = keyboardController,
        onFilterStateChange = { showBottomSheet = it },
        showBottomSheet = showBottomSheet
    )
}


@Composable
fun FavouritePhoneLayout(
    listState: LazyListState = rememberLazyListState(),
    data: List<Restaurant>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    onFilterStateChange: (Boolean) -> Unit,
    showBottomSheet: Boolean
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically)
        {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { onSearchQueryChange(it) },
                label = { Text("Browse restaurants") },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        keyboardController?.hide()
                    }
                )
            )
            FilterButton(
                text = "Filter",
                onClick = { onFilterStateChange(true) }
            )
        }

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

        if (showBottomSheet) {
            FilterBottomSheet(
                onDismiss = { onFilterStateChange(false) }
            )
        }
    }
}