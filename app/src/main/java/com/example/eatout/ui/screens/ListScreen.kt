package com.example.eatout.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.runtime.DisposableEffect
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
import com.example.eatout.domain.model.Restaurant
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.example.eatout.data.Note
import com.example.eatout.data.repository.LocationRepository
import com.example.eatout.data.repository.RestaurantRepository
import com.example.eatout.ui.components.FilterBottomSheet
import com.example.eatout.util.LocalRepository
import com.example.eatout.util.RestaurantViewModelFactory
import com.example.eatout.viewmodel.ListType
import com.example.eatout.viewmodel.MainViewModel
import com.example.eatout.viewmodel.SortOption
import com.example.eatout.viewmodel.SortOrder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantListScreen(
    viewModel: RestaurantViewModel,
    locationRepository: LocationRepository,
    isTablet: Boolean,
    navController: NavHostController,
    listType: String = "ALL",
    showDistance: Boolean = false
) {
    val context = LocalContext.current

    // Launcher dla uprawnień
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) {
            locationRepository.startLocationUpdates()
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationRepository.startLocationUpdates()
        } else {
            launcher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
    }

    DisposableEffect(Unit) {
        onDispose { locationRepository.stopLocationUpdates() }
    }

    val targetType = when (listType) {
        "FAVORITES" -> ListType.FAVORITES
        "TO_VISIT" -> ListType.TO_VISIT
        else -> ListType.ALL
    }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val restaurantsWithDist by viewModel.restaurantsWithDistance.collectAsState()
    val filteredOnly by viewModel.filteredRestaurants.collectAsState()


    val displayData = if (showDistance) {
        restaurantsWithDist
    } else {
        filteredOnly.map { it to 0.0 }
    }
    LaunchedEffect(Unit) {
        viewModel.selectAll()
    }
    LaunchedEffect(Unit) {
        viewModel.setListType(targetType)
    }


    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var showBottomSheet by remember { mutableStateOf(false) }

    val labels by viewModel.allLabels.collectAsState()
    val selectedTags by viewModel.selectedTags.collectAsState()
    val currentSort by viewModel.currentSort.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()

    RestaurantListScreenPhoneLayout(
        navController = navController,
        listState = listState,
        data = displayData,
        searchQuery = searchQuery,
        onSearchQueryChange = { viewModel.onSearchQueryChange(it)},
        keyboardController = keyboardController,
        onFilterStateChange = { showBottomSheet = it },
        showBottomSheet = showBottomSheet,
        showDistance = showDistance,
        labels = labels,
        selectedOptions = selectedTags,
        onOptionToggled = { viewModel.toggleTag(it) },
        currentSort = currentSort,
        sortOrder = sortOrder,
        onSortOrderToggled = { viewModel.toggleSortOrder() },
        viewModel = viewModel
    )
}


@Composable
fun RestaurantListScreenPhoneLayout(
    navController: NavHostController,
    listState: LazyListState = rememberLazyListState(),
    data: List<Pair<Restaurant, Double>>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    onFilterStateChange: (Boolean) -> Unit,
    showBottomSheet: Boolean,
    viewModel: RestaurantViewModel,
    showDistance: Boolean,
    labels: List<String>,
    selectedOptions: List<String>,
    onOptionToggled: (String) -> Unit,
    currentSort: SortOption,
    sortOrder: SortOrder,
    onSortOrderToggled: () -> Unit
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
                onRestaurantSelected = { restaurant -> navController.navigate("details/${restaurant.id}") },
                listState = listState,
                viewModel = viewModel,
                showDistance = showDistance
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
                onDismiss = { onFilterStateChange(false) },
                labels = labels,
                selectedOptions = selectedOptions,
                onOptionToggled = onOptionToggled,
                currentSortOrder = sortOrder,
                onSortToggle = onSortOrderToggled
            )
        }
    }
}