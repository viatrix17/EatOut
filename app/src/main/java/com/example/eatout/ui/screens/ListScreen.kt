package com.example.eatout.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.eatout.ui.components.RestaurantsList
import com.example.eatout.ui.components.SimpleVerticalScrollbar
import com.example.eatout.viewmodel.RestaurantViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import com.example.eatout.ui.components.FilterButton
import com.example.eatout.domain.model.Restaurant
import androidx.compose.runtime.setValue
import com.example.eatout.data.repository.RestaurantRepository
import com.example.eatout.ui.components.FilterBottomSheet
import com.example.eatout.util.LocalRepository
import com.example.eatout.util.RestaurantViewModelFactory
import com.example.eatout.viewmodel.ListType
import com.example.eatout.viewmodel.NoteViewModel


class ulubione(){
    companion object {
        var favs: ArrayList<Restaurant> = arrayListOf<Restaurant>()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantListScreen(
    viewModel: RestaurantViewModel,
    isTablet: Boolean,
    navController: NavHostController,
    listType: String = "ALL",
    showDistance: Boolean = false,
    noteViewModel: NoteViewModel,
    favourites: Boolean
) {
    val targetType = when (listType) {
        "FAVORITES" -> ListType.FAVORITES
        "TO_VISIT" -> ListType.TO_VISIT
        else -> ListType.ALL
    }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val restaurants by viewModel.filteredRestaurants.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.selectAll()
    }
    LaunchedEffect(Unit) {
        viewModel.setListType(targetType)
    }


    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var showBottomSheet by remember { mutableStateOf(false) }

    RestaurantListScreenPhoneLayout(
        navController = navController,
        listState = listState,
        data = restaurants,
        searchQuery = searchQuery,
        onSearchQueryChange = { viewModel.onSearchQueryChange(it)},
        keyboardController = keyboardController,
        onFilterStateChange = { showBottomSheet = it },
        showBottomSheet = showBottomSheet,
        viewModel = viewModel,
        showDistance = showDistance,
        noteViewModel = noteViewModel,
        favourites =favourites
    )
}


@Composable
fun RestaurantListScreenPhoneLayout(
    navController: NavHostController,
    listState: LazyListState = rememberLazyListState(),
    data: List<Restaurant>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    onFilterStateChange: (Boolean) -> Unit,
    showBottomSheet: Boolean,
    viewModel: RestaurantViewModel,
    showDistance: Boolean,
    noteViewModel: NoteViewModel,
    favourites: Boolean
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(scope) {
        var tmp = noteViewModel.getNotes("favourites")
        ulubione.favs = arrayListOf<Restaurant>()
        for(i in tmp){
            var dodaj : Restaurant = Restaurant(id=i.id, name=i.restauracja, address = i.lokalizacja, lon=i.lon, lan=i.lan)
            ulubione.favs.add(dodaj)
        }
    }
    var d = data
    if(favourites==true){
        d= ulubione.favs
    }
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
                data = d,
                onRestaurantSelected = { restaurant -> navController.navigate("details/${restaurant.id}") },
                listState = listState,
                viewModel = viewModel,
                showDistance = showDistance,
                noteViewModel = noteViewModel
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