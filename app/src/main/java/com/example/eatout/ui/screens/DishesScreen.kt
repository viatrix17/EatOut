package com.example.eatout.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.eatout.data.model.Note
import com.example.eatout.ui.components.DishesList
import com.example.eatout.ui.components.FilterBottomSheet
import com.example.eatout.ui.components.FilterButton
import com.example.eatout.ui.components.SimpleVerticalScrollbar
import com.example.eatout.domain.model.Dish
import com.example.eatout.viewmodel.DishViewModel
import com.example.eatout.viewmodel.DishesListType
import com.example.eatout.viewmodel.NoteViewModel
import com.example.eatout.viewmodel.SortOption
import com.example.eatout.viewmodel.SortOrder

@Composable
fun DishesScreen(
    viewModel: DishViewModel,
    isTablet: Boolean,
    navController: NavHostController,
    listType: String = "ALL"
) {
    val targetType = when (listType) {
        "FAVORITES" -> DishesListType.FAVORITES
        "TO_TRY" -> DishesListType.TO_TRY
        else -> DishesListType.ALL
    }

    val searchQuery by viewModel.searchQuery.collectAsState()
    val dishes by viewModel.filteredDishes.collectAsState()

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

    DishesPhoneLayout(
        listState = listState,
        data = dishes,
        searchQuery = searchQuery,
        onSearchQueryChange = { viewModel.onSearchQueryChange(it)},
        keyboardController = keyboardController,
        onFilterStateChange = { showBottomSheet = it },
        showBottomSheet = showBottomSheet,
        viewModel = viewModel,
        labels = labels,
        selectedOptions = selectedTags,
        onOptionToggled = { viewModel.toggleTag(it) },
        currentSort = currentSort,
        sortOrder = sortOrder,
        onSortOrderToggled = { viewModel.toggleSortOrder() },

    )
}

@Composable
fun DishesPhoneLayout(
    listState: LazyListState = rememberLazyListState(),
    data: List<Dish>,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    keyboardController: SoftwareKeyboardController?,
    onFilterStateChange: (Boolean) -> Unit,
    showBottomSheet: Boolean,
    viewModel: DishViewModel,
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
            verticalAlignment = Alignment.CenterVertically
        )
        {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { onSearchQueryChange(it) },
                label = { Text("Browse dishes") },
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
            DishesList(
                data = data,
                listState = listState,
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
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
