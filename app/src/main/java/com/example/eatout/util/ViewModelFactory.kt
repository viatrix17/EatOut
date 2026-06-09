package com.example.eatout.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.eatout.data.repository.LocationRepository
import com.example.eatout.data.repository.RestaurantRepository
import com.example.eatout.viewmodel.MainViewModel
import com.example.eatout.viewmodel.RestaurantViewModel

class MainViewModelFactory(
    private val locationRepository: LocationRepository,
    private val restaurantRepository: RestaurantRepository,
    private val preferencesManager: PreferencesManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(locationRepository, restaurantRepository, preferencesManager) as T
    }
}

class RestaurantViewModelFactory(
    private val repository: RestaurantRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RestaurantViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RestaurantViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}