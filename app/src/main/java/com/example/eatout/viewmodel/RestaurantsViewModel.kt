package com.example.eatout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class Restaurant( // mock class and data
    val id: Int,
    val name: String,
    val location: String,
    val rating: Double,      // ocena w skali 1-5
    val cuisineType: String  // np. "Włoska", "Sushi"
)

class RestaurantViewModel : ViewModel() {
    private val allRestaurants = listOf(
        Restaurant(1, "Pizzeria Da Grasso", "Poznań, Głogowska", 4.5, "Włoska"),
        Restaurant(2, "Restauracja Ratuszowa", "Poznań, Stary Rynek", 4.8, "Polska"),
        Restaurant(3, "Sushi Nami", "Poznań, Jeżyce", 4.2, "Japońska"),
        Restaurant(4, "Burgerownia Stacja", "Poznań, Wilda", 4.6, "Amerykańska"),
        Restaurant(5, "Falafel House", "Poznań, Centrum", 4.0, "Bliskowschodnia"),
        Restaurant(6, "Tajski Wok", "Poznań, Garbary", 4.7, "Tajska"),
        Restaurant(7, "La Rambla", "Poznań, Śródka", 4.4, "Hiszpańska"),
        Restaurant(8, "Pierogarnia u Mamy", "Poznań, Jeżyce", 4.9, "Polska"),
        Restaurant(9, "Vegan Ramen Shop", "Poznań, Centrum", 4.8, "Japońska"),
        Restaurant(10, "Kebab u Turka", "Poznań, Grunwald", 3.8, "Turecka"),
        Restaurant(11, "Greckie Smaki", "Poznań, Łazarz", 4.3, "Grecka"),
        Restaurant(12, "Francuska Bagietka", "Poznań, Rataje", 4.1, "Francuska"),
        Restaurant(13, "Indyjskie Curry", "Poznań, Wilda", 4.5, "Indyjska"),
        Restaurant(14, "VietStreet", "Poznań, Głogowska", 4.6, "Wietnamska"),
        Restaurant(15, "Steakhouse Prime", "Poznań, Centrum", 4.9, "Amerykańska")
    )

    private val _allRoutes = MutableStateFlow<List<Restaurant>>(emptyList())
    private val _currentRestaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val currentRestaurants = _currentRestaurants.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val filteredRestaurants = combine(_currentRestaurants, _searchQuery) { current, query ->
        if (query.isNotEmpty()) {
            allRestaurants.filter { it.name.contains(query, ignoreCase = true) }
        } else {
            current
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList())

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun selectAll() {
        _currentRestaurants.value = allRestaurants
        _searchQuery.value = ""
    }
}