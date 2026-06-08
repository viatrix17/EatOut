package com.example.eatout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

data class Restaurant( // mock class and data
    val id: Int,
    val name: String,
    val location: String,
    val rating: Double,
    val cuisineType: String,
    val tags: List<String>,
    val isToVisit: Boolean = false,
    val isFavorite: Boolean = false
)

enum class ListType { ALL, TO_VISIT, FAVORITES }

enum class SortOption { NAME }

enum class SortOrder { ASC, DESC }

class RestaurantViewModel : ViewModel() {
    private val allRestaurants = listOf(
        Restaurant(1, "Pizzeria Da Grasso", "Poznań, Głogowska", 4.5, "Włoska", listOf("włoskie", "pizza"), true, false),
        Restaurant(2, "Restauracja Ratuszowa", "Poznań, Stary Rynek", 4.8, "Polska", listOf("klasyczne", "restauracja"), false, true),
        Restaurant(3, "Sushi Nami", "Poznań, Jeżyce", 4.2, "Japońska", listOf("azjatyckie", "sushi-bar"), true, true),
        Restaurant(4, "Burgerownia Stacja", "Poznań, Wilda", 4.6, "Amerykańska", listOf("street-food", "burgery"), false, false),
        Restaurant(5, "Falafel House", "Poznań, Centrum", 4.0, "Bliskowschodnia", listOf("wegetariańskie", "street-food"), true, false),
        Restaurant(6, "Tajski Wok", "Poznań, Garbary", 4.7, "Tajska", listOf("azjatyckie", "street-food"), false, true),
        Restaurant(7, "La Rambla", "Poznań, Śródka", 4.4, "Hiszpańska", listOf("tapas-bar", "klimatyczne"), true, true),
        Restaurant(8, "Pierogarnia u Mamy", "Poznań, Jeżyce", 4.9, "Polska", listOf("domowe", "śniadaniownia"), true, false),
        Restaurant(9, "Vegan Ramen Shop", "Poznań, Centrum", 4.8, "Japońska", listOf("azjatyckie", "wegetariańskie"), false, false),
        Restaurant(10, "Kebab u Turka", "Poznań, Grunwald", 3.8, "Turecka", listOf("street-food", "fast-food"), false, true),
        Restaurant(11, "Greckie Smaki", "Poznań, Łazarz", 4.3, "Grecka", listOf("klasyczne", "wegetariańskie"), true, true),
        Restaurant(12, "Francuska Bagietka", "Poznań, Rataje", 4.1, "Francuska", listOf("kawiarnia", "śniadaniownia"), false, false),
        Restaurant(13, "Indyjskie Curry", "Poznań, Wilda", 4.5, "Indyjska", listOf("azjatyckie", "klasyczne"), true, false),
        Restaurant(14, "VietStreet", "Poznań, Głogowska", 4.6, "Wietnamska", listOf("azjatyckie", "street-food"), false, true),
        Restaurant(15, "Steakhouse Prime", "Poznań, Centrum", 4.9, "Amerykańska", listOf("steakhouse", "klasyczne"), true, false)
    )

    private val _allRestaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    private val _currentRestaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val currentRestaurants = _currentRestaurants.asStateFlow()

    private val _listType = MutableStateFlow(ListType.ALL)
    val listType = _listType.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    fun setListType(type: ListType) {
        _listType.value = type
    }

    private val _selectedTags = MutableStateFlow<List<String>>(emptyList())
    val selectedTags = _selectedTags.asStateFlow()

    fun toggleTag(tag: String) {
        _selectedTags.update { current ->
            if (current.contains(tag)) current - tag else current + tag
        }
    }

    private val _currentSort = MutableStateFlow(SortOption.NAME)
    val currentSort = _currentSort.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.ASC)
    val sortOrder = _sortOrder.asStateFlow()

    fun toggleSortOrder() {
        _sortOrder.update { if (it == SortOrder.ASC) SortOrder.DESC else SortOrder.ASC }
    }

    val filteredRestaurants = combine(
        _listType,
        _searchQuery,
        _selectedTags,
        _currentSort,
        _sortOrder
    ) { type, query, tags, sort, order ->

        // 1. Podstawowe filtrowanie (ListType)
        var list = when (type) {
            ListType.ALL -> allRestaurants
            ListType.TO_VISIT -> allRestaurants.filter { it.isToVisit }
            ListType.FAVORITES -> allRestaurants.filter { it.isFavorite }
        }

        // 2. Filtrowanie tekstowe
        if (query.isNotBlank()) {
            list = list.filter { it.name.contains(query, ignoreCase = true) }
        }

        // 3. Filtrowanie po tagach
        if (tags.isNotEmpty()) {
            list = list.filter { restaurant ->
                restaurant.tags.any { it in tags }
            }
        }

        // 4. Sortowanie
        // Obecnie mamy tylko SortOption.NAME, więc używamy tylko tego
        val sortedList = if (order == SortOrder.ASC) {
            list.sortedBy { it.name.lowercase() }
        } else {
            list.sortedByDescending { it.name.lowercase() }
        }

        sortedList
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = allRestaurants
    )


    val allLabels: StateFlow<List<String>> = filteredRestaurants
        .map { restaurants ->
            restaurants
                .flatMap { it.tags }
                .distinct()
                .sorted()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun selectAll() {
        _currentRestaurants.value = allRestaurants
        _searchQuery.value = ""
    }

    fun getRestaurantById(id: Int): Restaurant? {
        return allRestaurants.find { it.id == id }
    }

    fun toggleFavourite(id: Int){
        // TO DO DODAĆ ŻEBY ZMIENIAŁO FAVOURITE - wywoływanie funkcji z modelu (klasy)
    }

    fun toggleToVisit(id: Int){
        // TO DO DODAĆ ŻEBY ZMIENIAŁO FAVOURITE - wywoływanie funkcji z modelu (klasy)
    }

}