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
    val isFavorite: Boolean = false,
    var lan: Double = 0.0,
    var lon: Double = 0.0
)

enum class ListType { ALL, TO_VISIT, FAVORITES }

class RestaurantViewModel : ViewModel() {


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

    companion object {
        public var allRestaurants : ArrayList<Restaurant> = arrayListOf()
    }

}