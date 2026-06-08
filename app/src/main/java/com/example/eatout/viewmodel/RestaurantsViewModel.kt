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
    val tags: List<String>,
    val isToVisit: Boolean = false,
    val isFavorite: Boolean = false
)

enum class ListType { ALL, TO_VISIT, FAVORITES }

class RestaurantViewModel : ViewModel() {
    private val allRestaurants = listOf(
        Restaurant(1, "Pizzeria Da Grasso", "Poznań, Głogowska", 4.5, "Włoska", listOf("włoskie", "pizza")),
        Restaurant(2, "Restauracja Ratuszowa", "Poznań, Stary Rynek", 4.8, "Polska", listOf("klasyczne", "restauracja")),
        Restaurant(3, "Sushi Nami", "Poznań, Jeżyce", 4.2, "Japońska", listOf("azjatyckie", "sushi-bar")),
        Restaurant(4, "Burgerownia Stacja", "Poznań, Wilda", 4.6, "Amerykańska", listOf("street-food", "burgery")),
        Restaurant(5, "Falafel House", "Poznań, Centrum", 4.0, "Bliskowschodnia", listOf("wegetariańskie", "street-food")),
        Restaurant(6, "Tajski Wok", "Poznań, Garbary", 4.7, "Tajska", listOf("azjatyckie", "street-food")),
        Restaurant(7, "La Rambla", "Poznań, Śródka", 4.4, "Hiszpańska", listOf("tapas-bar", "klimatyczne")),
        Restaurant(8, "Pierogarnia u Mamy", "Poznań, Jeżyce", 4.9, "Polska", listOf("domowe", "śniadaniownia")),
        Restaurant(9, "Vegan Ramen Shop", "Poznań, Centrum", 4.8, "Japońska", listOf("azjatyckie", "wegetariańskie")),
        Restaurant(10, "Kebab u Turka", "Poznań, Grunwald", 3.8, "Turecka", listOf("street-food", "fast-food")),
        Restaurant(11, "Greckie Smaki", "Poznań, Łazarz", 4.3, "Grecka", listOf("klasyczne", "wegetariańskie")),
        Restaurant(12, "Francuska Bagietka", "Poznań, Rataje", 4.1, "Francuska", listOf("kawiarnia", "śniadaniownia")),
        Restaurant(13, "Indyjskie Curry", "Poznań, Wilda", 4.5, "Indyjska", listOf("azjatyckie", "klasyczne")),
        Restaurant(14, "VietStreet", "Poznań, Głogowska", 4.6, "Wietnamska", listOf("azjatyckie", "street-food")),
        Restaurant(15, "Steakhouse Prime", "Poznań, Centrum", 4.9, "Amerykańska", listOf("steakhouse", "klasyczne"))
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