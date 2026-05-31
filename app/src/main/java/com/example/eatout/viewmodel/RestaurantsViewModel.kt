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
    val rating: Double,
    val cuisineType: String,
    val isToVisit: Boolean = false,
    val isFavorite: Boolean = false
)

enum class ListType { ALL, TO_VISIT, FAVORITES }

class RestaurantViewModel : ViewModel() {
    private val allRestaurants = listOf(
        Restaurant(
            1,
            "Pizzeria Da Grasso",
            "Poznań, Głogowska",
            4.5,
            "Włoska",
            isToVisit = true,
            isFavorite = true
        ),
        Restaurant(
            2,
            "Restauracja Ratuszowa",
            "Poznań, Stary Rynek",
            4.8,
            "Polska",
            isToVisit = false,
            isFavorite = true
        ),
        Restaurant(
            3,
            "Sushi Nami",
            "Poznań, Jeżyce",
            4.2,
            "Japońska",
            isToVisit = true,
            isFavorite = false
        ),
        Restaurant(
            4,
            "Burgerownia Stacja",
            "Poznań, Wilda",
            4.6,
            "Amerykańska",
            isToVisit = true,
            isFavorite = false
        ),
        Restaurant(
            5,
            "Falafel House",
            "Poznań, Centrum",
            4.0,
            "Bliskowschodnia",
            isToVisit = false,
            isFavorite = false
        ),
        Restaurant(
            6,
            "Tajski Wok",
            "Poznań, Garbary",
            4.7,
            "Tajska",
            isToVisit = true,
            isFavorite = true
        ),
        Restaurant(
            7,
            "La Rambla",
            "Poznań, Śródka",
            4.4,
            "Hiszpańska",
            isToVisit = false,
            isFavorite = false
        ),
        Restaurant(
            8,
            "Pierogarnia u Mamy",
            "Poznań, Jeżyce",
            4.9,
            "Polska",
            isToVisit = true,
            isFavorite = true
        ),
        Restaurant(
            9,
            "Vegan Ramen Shop",
            "Poznań, Centrum",
            4.8,
            "Japońska",
            isToVisit = false,
            isFavorite = true
        ),
        Restaurant(
            10,
            "Kebab u Turka",
            "Poznań, Grunwald",
            3.8,
            "Turecka",
            isToVisit = false,
            isFavorite = false
        ),
        Restaurant(
            11,
            "Greckie Smaki",
            "Poznań, Łazarz",
            4.3,
            "Grecka",
            isToVisit = true,
            isFavorite = false
        ),
        Restaurant(
            12,
            "Francuska Bagietka",
            "Poznań, Rataje",
            4.1,
            "Francuska",
            isToVisit = false,
            isFavorite = false
        ),
        Restaurant(
            13,
            "Indyjskie Curry",
            "Poznań, Wilda",
            4.5,
            "Indyjska",
            isToVisit = true,
            isFavorite = false
        ),
        Restaurant(
            14,
            "VietStreet",
            "Poznań, Głogowska",
            4.6,
            "Wietnamska",
            isToVisit = true,
            isFavorite = false
        ),
        Restaurant(
            15,
            "Steakhouse Prime",
            "Poznań, Centrum",
            4.9,
            "Amerykańska",
            isToVisit = false,
            isFavorite = true
        )
    )

    private val _allRestaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    private val _currentRestaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val currentRestaurants = _currentRestaurants.asStateFlow()

    private val _listType = MutableStateFlow(ListType.ALL)
    val listType = _listType.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val filteredRestaurants = combine(_listType, _searchQuery) { type, query ->
        val baseList = when (type) {
            ListType.ALL -> allRestaurants
            ListType.TO_VISIT -> allRestaurants.filter { it.isToVisit }
            ListType.FAVORITES -> allRestaurants.filter { it.isFavorite }
        }
        if (query.isBlank()) baseList
        else baseList.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = allRestaurants
    )

    fun setListType(type: ListType) {
        _listType.value = type
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun selectAll() {
        _currentRestaurants.value = allRestaurants
        _searchQuery.value = ""
    }

}