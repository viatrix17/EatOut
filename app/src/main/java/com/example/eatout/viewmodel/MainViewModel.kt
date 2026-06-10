package com.example.eatout.viewmodel

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewModelScope
import com.example.eatout.util.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import android.location.Location
import com.example.eatout.data.repository.LocationRepository
import com.example.eatout.data.repository.RestaurantRepository
import com.example.eatout.domain.model.Restaurant
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.combine
import android.util.Log

class MainViewModel(
    private val locationRepository: LocationRepository,
    private val restaurantRepository: RestaurantRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation = _userLocation.asStateFlow()

    private val _restaurants = MutableStateFlow<List<Restaurant>>(emptyList())
    val restaurants = _restaurants.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isDarkThemeCustom = MutableStateFlow<Boolean?>(null)
    val isDarkThemeCustom = _isDarkThemeCustom.asStateFlow()

    private val _isSystemTheme = MutableStateFlow<Boolean?>(null)
    val isSystemTheme = _isSystemTheme.asStateFlow()

    private val _selectedCity = MutableStateFlow("Poznań")
    private val _selectedCountry = MutableStateFlow("Polska")
    private val _isLocationEnabled = MutableStateFlow(false)

    val selectedCity = _selectedCity.asStateFlow()
    val selectedCountry = _selectedCountry.asStateFlow()
    val isLocationEnabled = _isLocationEnabled.asStateFlow()

    init {
        viewModelScope.launch {
            locationRepository.startLocationUpdates()
        }

        viewModelScope.launch {
            preferencesManager.getDarkMode().collect { darkMode ->
                _isDarkThemeCustom.value = darkMode
            }
        }
        viewModelScope.launch {
            preferencesManager.getUseSystemTheme().collect { useSystem ->
                _isSystemTheme.value = useSystem
            }
        }
        viewModelScope.launch {
            combine(selectedCity, selectedCountry, isLocationEnabled) { city, country, locationEnabled ->
                Triple(city, country, locationEnabled)
            }.collect { (city, country, locationEnabled) ->
                fetchData(city, country, locationEnabled)
            }
        }
    }

    private suspend fun fetchData(city: String, country: String, isLocationEnabled: Boolean) {
        Log.d("DEBUG_EATOUT", "Rozpoczynam pobieranie danych dla: $city")
        _isLoading.value = true
        try {
            val data = restaurantRepository.fetchRestaurants() ?: emptyList() //city, country)
            Log.d("DEBUG_EATOUT", "Pobrano ${data.size} restauracji")
            _restaurants.value = data
        } catch (e: Exception) {
            Log.e("DEBUG_EATOUT", "Błąd pobierania: ${e.message}")
            _restaurants.value = emptyList()
        } finally {
            _isLoading.value = false
        }
    }


    fun changeToDarkMode() {
        viewModelScope.launch {
            preferencesManager.saveUseSystemTheme(false)
            preferencesManager.saveDarkMode(true)
            _isSystemTheme.value = false
            _isDarkThemeCustom.value = true
        }
    }

    fun changeToLightMode(){
        viewModelScope.launch {
            preferencesManager.saveUseSystemTheme(false)
            preferencesManager.saveDarkMode(false)
            _isSystemTheme.value = false
            _isDarkThemeCustom.value = false

        }
    }

    fun enableSystemMode(){
        viewModelScope.launch {
            preferencesManager.saveUseSystemTheme(true)
            _isSystemTheme.value = true
        }
    }

    fun updateCity(newCity: String) {
        _selectedCity.value = newCity
    }

    fun updateCountry(newCountry: String) {
        _selectedCountry.value = newCountry
    }

    fun toggleLocationEnabled() {
        _isLocationEnabled.value = !_isLocationEnabled.value
    }

}