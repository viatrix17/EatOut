package com.example.eatout.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatout.domain.model.Dish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update


enum class DishesListType { ALL, TO_TRY, FAVORITES }

class DishViewModel : ViewModel() {

    private val allDishes = MutableStateFlow(listOf(
        // 1: Pizzeria Da Grasso
        Dish(
            1,
            1,
            "Pizza Margherita",
            32.0,
            "sos pomidorowy, mozzarella, bazylia",
            listOf("włoskie", "wegetariańskie"),
            false,
            false
        ),
        Dish(2, 1, "Pizza Pepperoni", 38.0, "sos pomidorowy, mozzarella, pepperoni", listOf("mięsne", "ostre"), false, false),
        Dish(3, 1, "Pizza Capricciosa", 40.0, "sos pomidorowy, mozzarella, szynka, pieczarki", listOf("włoskie", "klasyczne"), false, false),
        Dish(46, 1, "Spaghetti Carbonara", 36.0, "jajka, ser pecorino, guanciale, pieprz", listOf("włoskie", "klasyczne"), false, false),
        Dish(47, 1, "Lasagne al Forno", 42.0, "makaron, ragu wołowe, beszamel, parmezan", listOf("włoskie", "mięsne"), false, false),
        Dish(48, 1, "Risotto ai Funghi", 39.0, "ryż arborio, podgrzybki, cebula, białe wino, masło", listOf("włoskie", "wegetariańskie"), false, false),
        Dish(49, 1, "Gnocchi al Pomodoro", 34.0, "kluseczki ziemniaczane, sos pomidorowy, bazylia, mozzarella", listOf("włoskie", "wegetariańskie"), false, false),
        Dish(50, 1, "Bruschetta Classica", 22.0, "grzanki, pomidory, czosnek, bazylia, oliwa extra virgin", listOf("włoskie", "przystawki"), false, false),
        Dish(51, 1, "Tiramisù", 26.0, "mascarpone, biszkopty, espresso, kakao", listOf("włoskie", "desery"), false, false),
        Dish(52, 1, "Tagliatelle al Tartufo", 45.0, "makaron wstążki, krem truflowy, parmezan", listOf("włoskie", "wykwintne"), false, false),

        // 2: Restauracja Ratuszowa
        Dish(4, 2, "Pierogi Ruskie", 28.0, "ciasto, twaróg, ziemniaki, cebulka", listOf("polskie", "wegetariańskie"), false, false),
        Dish(5, 2, "Żurek w chlebie", 22.0, "zakwas, biała kiełbasa, jajko, chleb", listOf("polskie", "rozgrzewające"), false, false),
        Dish(6, 2, "Kotlet Schabowy", 42.0, "schab, panierka, ziemniaki, mizeria", listOf("polskie", "klasyczne"), false, false),

        // 3: Sushi Nami
        Dish(7, 3, "California Roll", 45.0, "surimi, awokado, ogórek, ryż, nori", listOf("azjatyckie", "sushi"), false, false),
        Dish(8, 3, "Miso Soup", 15.0, "pasta miso, tofu, glony wakame", listOf("azjatyckie", "lekkie"), false, false),
        Dish(9, 3, "Tempura Ebi", 35.0, "krewetki w cieście, sos tentsuyu", listOf("azjatyckie", "przystawka"), false, false),

        // 4: Burgerownia Stacja
        Dish(10, 4, "Klasyczny Wołowy", 35.0, "wołowina, cheddar, sałata, sos", listOf("mięsne", "street-food"), false, false),
        Dish(11, 4, "Chilli Burger", 39.0, "wołowina, papryczki jalapeño, sos spicy", listOf("mięsne", "ostre"), false, false),
        Dish(12, 4, "Frytki z batatów", 12.0, "frytki z batatów, sól morska", listOf("wegetariańskie", "dodatki"), false, false),

        // 5: Falafel House
        Dish(13, 5, "Falafel Wrap", 20.0, "kulki z ciecierzycy, warzywa, sos tahini", listOf("wegetariańskie", "street-food"), false, false),
        Dish(14, 5, "Hummus Plate", 18.0, "pasta z ciecierzycy, oliwa, pita", listOf("wegetariańskie", "zdrowe"), false, false),
        Dish(15, 5, "Szakszuka", 26.0, "jajka, pomidory, papryka, przyprawy", listOf("wegetariańskie", "śniadaniowe"), false, false),

        // 6: Tajski Wok
        Dish(16, 6, "Pad Thai", 34.0, "makaron ryżowy, orzeszki, krewetki/kurczak", listOf("azjatyckie", "klasyczne"), false, false),
        Dish(17, 6, "Green Curry", 36.0, "zielone curry, mleko kokosowe, warzywa", listOf("azjatyckie", "ostre"), false, false),
        Dish(18, 6, "Tom Yum", 28.0, "zupa kwaśno-pikantna, trawa cytrynowa", listOf("azjatyckie", "ostre"), false, false),

        // 7: La Rambla
        Dish(19, 7, "Patatas Bravas", 18.0, "ziemniaki, sos pikantny, aioli", listOf("hiszpańskie", "tapas"), true, false),
        Dish(20, 7, "Paella Seafood", 55.0, "ryż szafranowy, owoce morza", listOf("hiszpańskie", "klasyczne"), false, false),
        Dish(21, 7, "Chorizo al Vino", 32.0, "kiełbaska chorizo w winie", listOf("hiszpańskie", "mięsne"), false, false),

        // 8: Pierogarnia u Mamy
        Dish(22, 8, "Pierogi z jagodami", 25.0, "ciasto, owoce, śmietana", listOf("polskie", "domowe"), false, false),
        Dish(23, 8, "Pierogi z mięsem", 26.0, "ciasto, wieprzowina, okrasa", listOf("polskie", "domowe"), false, false),
        Dish(24, 8, "Naleśniki z twarogiem", 22.0, "ciasto, twaróg, cukier puder", listOf("polskie", "śniadaniowe"), false, false),

        // 9: Vegan Ramen Shop
        Dish(25, 9, "Vegan Shio Ramen", 39.0, "bulion warzywny, makaron, nori", listOf("azjatyckie", "wegańskie"), true, false),
        Dish(26, 9, "Edamame", 14.0, "młoda soja, sól morska", listOf("azjatyckie", "przystawka"), false, false),
        Dish(27, 9, "Spicy Miso Ramen", 42.0, "pikantny bulion, tofu, kukurydza", listOf("azjatyckie", "ostre"), false, false),

        // 10: Kebab u Turka
        Dish(28, 10, "Kebab w bułce", 22.0, "mięso, surówki, sos mieszany", listOf("street-food", "fast-food"), true, false),
        Dish(29, 10, "Kebab box", 24.0, "mięso, frytki, sosy", listOf("street-food", "fast-food"), false, false),
        Dish(30, 10, "Rollo duże", 26.0, "tortilla, mięso, ser, warzywa", listOf("street-food", "mięsne"), false, false),

        // 11: Greckie Smaki
        Dish(31, 11, "Sałatka Grecka", 24.0, "feta, oliwki, pomidor, ogórek", listOf("greckie", "wegetariańskie"), true, false),
        Dish(32, 11, "Souvlaki", 32.0, "szaszłyk drobiowy, tzatziki, pita", listOf("greckie", "mięsne"), false, false),
        Dish(33, 11, "Moussaka", 36.0, "bakłażan, mięso mielone, sos beszamel", listOf("greckie", "klasyczne"), false, false),

        // 12: Francuska Bagietka
        Dish(34, 12, "Croissant Masło", 10.0, "tradycyjny rogal francuski", listOf("francuskie", "kawiarnia"), false, false),
        Dish(35, 12, "Bagietka Szynka/Ser", 16.0, "bagietka, masło, ser, szynka", listOf("francuskie", "śniadaniowe"), false, false),
        Dish(36, 12, "Quiche Lorraine", 22.0, "tarta, boczek, ser, śmietana", listOf("francuskie", "klasyczne"), false, false),

        // 13: Indyjskie Curry
        Dish(37, 13, "Butter Chicken", 38.0, "kurczak, kremowy sos pomidorowy", listOf("indyjskie", "mięsne"), false, false),
        Dish(38, 13, "Naan Czosnkowy", 8.0, "placek pieczony w piecu tandoor", listOf("indyjskie", "dodatki"), true, false),
        Dish(39, 13, "Palak Paneer", 34.0, "szpinak, ser paneer, przyprawy", listOf("indyjskie", "wegetariańskie"), false, false),

        // 14: VietStreet
        Dish(40, 14, "Pho Bo", 28.0, "bulion wołowy, makaron ryżowy", listOf("azjatyckie", "klasyczne"), false, false),
        Dish(41, 14, "Spring Rolls", 16.0, "roladki, warzywa, sos", listOf("azjatyckie", "przystawka"), false, false),
        Dish(42, 14, "Banh Mi", 24.0, "bagietka, pasztet, mięso, kolendra", listOf("azjatyckie", "street-food"), false, false),

        // 15: Steakhouse Prime
        Dish(43, 15, "Ribeye Steak", 85.0, "antrykot wołowy, masło ziołowe", listOf("mięsne", "steakhouse"), false, false),
        Dish(44, 15, "Grillowane Warzywa", 20.0, "sezonowe warzywa z grilla", listOf("wegetariańskie", "zdrowe"), false, false),
        Dish(45, 15, "Steak Tatar", 45.0, "siekana wołowina, dodatki", listOf("mięsne", "klasyczne"), false, false)
    )
    )

//    private val allDishes = MutableStateFlow<List<Dish>>(emptyList())
    private val _currentDishes = MutableStateFlow<List<Dish>>(emptyList())
    val currentDishes = _currentDishes.asStateFlow()

    private val _listType = MutableStateFlow(DishesListType.ALL)
    val listType = _listType.asStateFlow()
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites = _showOnlyFavorites.asStateFlow()

    fun toggleShowOnlyFavorites() {
        _showOnlyFavorites.update { !it }
    }
    val filteredDishes: StateFlow<List<Dish>> = combine(
        _listType,
        _searchQuery,
        _showOnlyFavorites,
        allDishes
    ) { type, query, showOnlyFavs, allDishesList ->

        var list = when (type) {
            DishesListType.ALL -> allDishesList
            DishesListType.TO_TRY -> allDishesList.filter { it.isToTry }
            DishesListType.FAVORITES -> allDishesList.filter { it.isFavorite }
        }

        if (showOnlyFavs) {
            list = list.filter { it.isFavorite }
        }

        if (query.isNotBlank()) {
            list = list.filter { it.name.contains(query, ignoreCase = true) }
        }

        list
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList() // Użycie emptyList() jest bezpieczniejsze na start
    )

    fun setListType(type: DishesListType) {
        _listType.value = type
    }

    fun onSearchQueryChange(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun selectAll() {
        _currentDishes.value = allDishes.value
        _searchQuery.value = ""
    }

    fun getDishesForRestaurant(id: Long): List<Dish> {
        return allDishes.value.filter { it.restaurantId == id }
    }
    fun toggleFavourite(id: Int){
        // TO DO DODAĆ ŻEBY ZMIENIAŁO FAVOURITE - wywoływanie funkcji z modelu (klasy)
    }

    fun toggleToTry(id: Int){
        // TO DO DODAĆ ŻEBY ZMIENIAŁO FAVOURITE - wywoływanie funkcji z modelu (klasy)
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

    val allLabels: StateFlow<List<String>> = allDishes.map { dishes ->
        dishes.flatMap { it.labels }.distinct().sorted()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

}