package com.example.eatout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class Dish(
    val id: Int,
    val name: String,
    val ingredients: String,
    val labels: List<String>,
    val isFavorite: Boolean,
    val isToTry: Boolean
)

enum class DishesListType { ALL, TO_TRY, FAVORITES }

class DishViewModel : ViewModel() {

    val allDishes = listOf( // MOCK DATA
        Dish(
            id = 1,
            name = "Margherita",
            ingredients = "Pomidory, mozzarella, bazylia, oliwa z oliwek",
            labels = listOf("Włoskie", "Wegetariańskie", "Pizza"),
            isFavorite = false,
            isToTry = false
        ),
        Dish(
            id = 2,
            name = "Pad Thai",
            ingredients = "Makaron ryżowy, krewetki, tofu, jajko, orzeszki ziemne, kiełki fasoli",
            labels = listOf("Tajskie", "Bezglutenowe", "Obiad"),
            isFavorite = true,
            isToTry = true
        ),
        Dish(
            id = 3,
            name = "Burger Klasyczny",
            ingredients = "Wołowina, bułka, sałata, pomidor, cebula, ogórek kiszony, sos",
            labels = listOf("Amerykańskie", "Mięsne", "Fast-food"),
            isFavorite = false,
            isToTry = true
        ),
        Dish(
            id = 4,
            name = "Sałatka Cezar",
            ingredients = "Kurczak, sałata rzymska, grzanki, parmezan, sos cezar",
            labels = listOf("Lekkie", "Mięsne", "Sałatka"),
            isFavorite = true,
            isToTry = false
        ),
        Dish(
            id = 5,
            name = "Bowl Wegański",
            ingredients = "Quinoa, awokado, edamame, marchew, sos sojowy, nasiona sezamu",
            labels = listOf("Wegańskie", "Zdrowe", "Bowl"),
            isFavorite = true,
            isToTry = false
        )
    )

    private val _allDishes = MutableStateFlow<List<Dish>>(emptyList())
    private val _currentDishes = MutableStateFlow<List<Dish>>(emptyList())
    val currentDishes = _currentDishes.asStateFlow()

    private val _listType = MutableStateFlow(DishesListType.ALL)
    val listType = _listType.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val filteredDishes = combine(_listType, _searchQuery) { type, query ->
        val baseList = when (type) {
            DishesListType.ALL -> allDishes
            DishesListType.TO_TRY -> allDishes.filter { it.isToTry }
            DishesListType.FAVORITES -> allDishes.filter { it.isFavorite }
        }
        if (query.isBlank()) baseList
        else baseList.filter { it.name.contains(query, ignoreCase = true) }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = allDishes
    )

    fun setListType(type: DishesListType) {
        _listType.value = type
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun selectAll() {
        _currentDishes.value = allDishes
        _searchQuery.value = ""
    }

    fun toggleFavourite(id: Int){
        // TO DO DODAĆ ŻEBY ZMIENIAŁO FAVOURITE - wywoływanie funkcji z modelu (klasy)
    }

    fun toggleToVisit(id: Int){
        // TO DO DODAĆ ŻEBY ZMIENIAŁO FAVOURITE - wywoływanie funkcji z modelu (klasy)
    }

}