package com.example.eatout.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.eatout.data.model.Note
import com.example.eatout.data.repository.NoteRepository
import com.example.eatout.data.repository.LocationRepository
import com.example.eatout.data.repository.RestaurantRepository
import com.example.eatout.domain.model.Restaurant
import com.example.eatout.domain.model.RestaurantUIState
import com.example.eatout.util.RestaurantProcessor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ListType { ALL, TO_VISIT, FAVORITES }

enum class SortOption { NAME }

enum class SortOrder { ASC, DESC }

class RestaurantViewModel(
    private val repository: RestaurantRepository,
    private val locationRepository: LocationRepository,
    private val noteRepository: NoteRepository
) : ViewModel() {

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

//    companion object {
//        fun provideFactory(repository: RestaurantRepository): ViewModelProvider.Factory =
//            object : ViewModelProvider.Factory {
//                @Suppress("UNCHECKED_CAST")
//                override fun <T : ViewModel> create(modelClass: Class<T>): T {
//                    return RestaurantViewModel(repository) as T
//                }
//            }
//    }

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

    val favoriteNotes = MutableStateFlow<List<Note>>(emptyList())
    val toVisitNotes = MutableStateFlow<List<Note>>(emptyList())

    val filteredRestaurants: StateFlow<List<RestaurantUIState>> = combine(
        _listType, _searchQuery, _selectedTags, _currentSort, _sortOrder, allRestaurants, favoriteNotes, toVisitNotes
    ) { args ->
        // Jawne rzutowanie każdego elementu tablicy args
        val type = args[0] as ListType
        val query = args[1] as String
        val tags = args[2] as List<String>
        val sort = args[3] as SortOption
        val order = args[4] as SortOrder
        val allRests = args[5] as List<Restaurant>
        val favs = args[6] as List<Note>
        val toVisit = args[7] as List<Note>
        val favNames = favs.map { it.restauracja }.toSet()
        val toVisitNames = toVisit.map { it.restauracja }.toSet()

        var list = allRests.map { rest ->
            RestaurantUIState(
                id = rest.id,
                name = rest.name,
                address = rest.address,
                cuisineType = rest.cuisineType,
                tags = rest.tags,
                lan = rest.lan,
                lon = rest.lon,
                isFavorite = favNames.contains(rest.name),
                isToVisit = toVisitNames.contains(rest.name)
            )
        }

        list = when (type) {
            ListType.ALL -> list
            ListType.TO_VISIT -> list.filter { it.isToVisit }
            ListType.FAVORITES -> list.filter { it.isFavorite }
        }

        if (query.isNotBlank()) {
            list = list.filter { it.name.contains(query, ignoreCase = true) }
        }

        if (tags.isNotEmpty()) {
            list = list.filter { uiState ->
                uiState.tags.any { it in tags }
            }
        }

        if (order == SortOrder.ASC) {
            list.sortedBy { it.name.lowercase() }
        } else {
            list.sortedByDescending { it.name.lowercase() }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
        // Uruchamiamy nasłuchiwanie w momencie stworzenia ViewModelu
        noteRepository.observeNotes("favourites", { notes ->
            favoriteNotes.value = notes
        }, {})

        noteRepository.observeNotes("rest_to_visit", { notes ->
            toVisitNotes.value = notes
        }, {})
    }
    val userLocation = locationRepository.location

    val restaurantsWithDistance: StateFlow<List<Pair<RestaurantUIState, Double>>> = combine(
        filteredRestaurants,
        userLocation
    ) { restaurants, location ->
        if (location != null) {
            Log.d("DEBUG_DIST", "Moja lokalizacja: ${location.latitude}, ${location.longitude}")
        } else {
            Log.d("DEBUG_DIST", "Lokalizacja jest NULL!")
        }
        if (location == null) {
            restaurants.map { it to 0.0 }
        } else {
            restaurants.map { restaurant ->
                val dist = RestaurantProcessor.calculateDistance(
                    location.latitude, location.longitude,
                    restaurant.lan, restaurant.lon
                )
                restaurant to dist
            }.sortedBy { it.second }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    val allLabels: StateFlow<List<String>> = allRestaurants
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

    fun getRestaurantById(id: Long): RestaurantUIState? {
        val restaurant = allRestaurants.value.find { it.id == id } ?: return null

        val isFav = favoriteNotes.value.any { it.restauracja == restaurant.name }
        val isToVisit = toVisitNotes.value.any { it.restauracja == restaurant.name }

        return RestaurantUIState(
            id = restaurant.id,
            name = restaurant.name,
            address = restaurant.address,
            cuisineType = restaurant.cuisineType,
            tags = restaurant.tags,
            lan = restaurant.lan,
            lon = restaurant.lon,
            isFavorite = isFav,
            isToVisit = isToVisit
        )
    }

    fun toggleFavourite(rest: RestaurantUIState) {
        val note = Note(
            restauracja = rest.name,
            lokalizacja = rest.address,
            tagi = rest.tags,
            lan = rest.lan,
            lon = rest.lon
        )
        viewModelScope.launch {
            noteRepository.toggleNote("favourites", rest.name, note)
        }
    }

    fun addToVisit(rest: RestaurantUIState) {
        val note = Note(
            restauracja = rest.name,
            lokalizacja = rest.address,
            tagi = rest.tags,
            lan = rest.lan,
            lon = rest.lon
        )
        viewModelScope.launch {
            noteRepository.addNote("rest_to_visit", note)
        }
    }

    fun removeFromToVisit(rest: RestaurantUIState) {
        viewModelScope.launch {
            noteRepository.removeNote("rest_to_visit", rest.name)
        }
    }


}