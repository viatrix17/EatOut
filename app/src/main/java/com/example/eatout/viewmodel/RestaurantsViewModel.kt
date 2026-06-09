package com.example.eatout.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.eatout.data.Note
import com.example.eatout.data.repository.RestaurantRepository
import com.example.eatout.domain.model.Restaurant
import com.example.eatout.util.RestaurantProcessor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

enum class ListType { ALL, TO_VISIT, FAVORITES }

enum class SortOption { NAME }

enum class SortOrder { ASC, DESC }

class RestaurantViewModel(private val repository: RestaurantRepository) : ViewModel() {

    private val allRestaurantsMock = listOf(
        Restaurant(
            1,
            "Pizzeria Da Grasso",
            "Poznań, Głogowska",
            "Włoska",
            listOf("włoskie", "pizza"),
            true,
            false
        ),
        Restaurant(2, "Restauracja Ratuszowa", "Poznań, Stary Rynek", "Polska", listOf("klasyczne", "restauracja"), false, true),
        Restaurant(3, "Sushi Nami", "Poznań, Jeżyce","Japońska", listOf("azjatyckie", "sushi-bar"), true, true),
        Restaurant(4, "Burgerownia Stacja", "Poznań, Wilda", "Amerykańska", listOf("street-food", "burgery"), false, false),
        Restaurant(5, "Falafel House", "Poznań, Centrum",  "Bliskowschodnia", listOf("wegetariańskie", "street-food"), true, false),
        Restaurant(6, "Tajski Wok", "Poznań, Garbary",  "Tajska", listOf("azjatyckie", "street-food"), false, true),
        Restaurant(7, "La Rambla", "Poznań, Śródka",  "Hiszpańska", listOf("tapas-bar", "klimatyczne"), true, true),
        Restaurant(8, "Pierogarnia u Mamy", "Poznań, Jeżyce",  "Polska", listOf("domowe", "śniadaniownia"), true, false),
        Restaurant(9, "Vegan Ramen Shop", "Poznań, Centrum", "Japońska", listOf("azjatyckie", "wegetariańskie"), false, false),
        Restaurant(10, "Kebab u Turka", "Poznań, Grunwald", "Turecka", listOf("street-food", "fast-food"), false, true),
        Restaurant(11, "Greckie Smaki", "Poznań, Łazarz", "Grecka", listOf("klasyczne", "wegetariańskie"), true, true),
        Restaurant(12, "Francuska Bagietka", "Poznań, Rataje",  "Francuska", listOf("kawiarnia", "śniadaniownia"), false, false),
        Restaurant(13, "Indyjskie Curry", "Poznań, Wilda",  "Indyjska", listOf("azjatyckie", "klasyczne"), true, false),
        Restaurant(14, "VietStreet", "Poznań, Głogowska",  "Wietnamska", listOf("azjatyckie", "street-food"), false, true),
        Restaurant(15, "Steakhouse Prime", "Poznań, Centrum",  "Amerykańska", listOf("steakhouse", "klasyczne"), true, false)
    )

    companion object {
        fun provideFactory(repository: RestaurantRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return RestaurantViewModel(repository) as T
                }
            }
    }

    val allRestaurants = repository.restaurantsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
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

    val filteredRestaurants: StateFlow<List<Restaurant>> = combine(
        _listType, _searchQuery, _selectedTags, _currentSort, _sortOrder, allRestaurants
    ) { args ->
        val type = args[0] as ListType
        val query = args[1] as String
        val tags = args[2] as List<String>
        val sort = args[3] as SortOption
        val order = args[4] as SortOrder
        val restaurants = args[5] as List<Restaurant>

        var list = when (type) {
            ListType.ALL -> restaurants
            ListType.TO_VISIT -> restaurants.filter { it.isToVisit }
            ListType.FAVORITES -> restaurants.filter { it.isFavorite }
        }

        if (query.isNotBlank()) {
            list = list.filter { it.name.contains(query, ignoreCase = true) }
        }

        if (tags.isNotEmpty()) {
            list = list.filter { restaurant ->
                restaurant.tags.any { it in tags }
            }
        }

        val sortedList = if (order == SortOrder.ASC) {
            list.sortedBy { it.name.lowercase() }
        } else {
            list.sortedByDescending { it.name.lowercase() }
        }

        sortedList
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
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
        _listType.value = ListType.ALL

        _searchQuery.value = ""

        _selectedTags.value = emptyList()
    }

    fun getRestaurantById(id: Long): Restaurant? {
        return allRestaurants.value.find { it.id == id }
    }


    fun toggleFavourite(id: Long){
        // TO DO DODAĆ ŻEBY ZMIENIAŁO FAVOURITE - wywoływanie funkcji z modelu (klasy)
    }

    fun toggleToVisit(id: Long){
        // TO DO DODAĆ ŻEBY ZMIENIAŁO FAVOURITE - wywoływanie funkcji z modelu (klasy)
    }

    fun addToFavourite(rest : Restaurant, noteViewModel: NoteViewModel){
        var note : Note = Note()
        note.restauracja = rest.name;
        note.lokalizacja = rest.address
        note.tagi = RestaurantProcessor.returnTag(rest.name)
        note.lon=rest.lon
        note.lan=rest.lon
        Log.d("TAG", "added something")
        noteViewModel.run {
            addNote(note, "favourites")
        }
    }

    fun addToVisit(rest : Restaurant, noteViewModel: NoteViewModel){
        var note : Note = Note()
        note.restauracja = rest.name;
        note.lokalizacja = rest.address
        note.tagi = RestaurantProcessor.returnTag(rest.name)
        note.lon=rest.lon
        note.lan=rest.lon
        Log.d("TAG", "added to visit")
        noteViewModel.run {
            addNote(note, "rest_to_visit")
        }
    }

}