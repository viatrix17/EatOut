package com.example.eatout.viewmodel

import android.app.Application
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.eatout.network.ApiService
import com.example.eatout.util.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isDarkThemeCustom = MutableStateFlow<Boolean?>(null)
    val isDarkThemeCustom = _isDarkThemeCustom.asStateFlow()

    private val _isSystemTheme = MutableStateFlow<Boolean?>(null)
    val isSystemTheme = _isSystemTheme.asStateFlow()

    init {
        viewModelScope.launch {
            PreferencesManager.getDarkMode(getApplication()).collect { darkMode ->
                _isDarkThemeCustom.value = darkMode
            }
        }
        viewModelScope.launch {
            PreferencesManager.getUseSystemTheme(getApplication()).collect { useSystem ->
                _isSystemTheme.value = useSystem
            }
        }
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch(){
            try {
                ApiService.fetchRestaurantsInPoznan()
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
            }
        }
    }

    fun changeToDarkMode(){
        viewModelScope.launch {
            PreferencesManager.saveUseSystemTheme(getApplication(), false)
            PreferencesManager.saveDarkMode(getApplication(), true)
            _isDarkThemeCustom.value = true
        }
    }

    fun changeToLightMode(){
        viewModelScope.launch {
            PreferencesManager.saveUseSystemTheme(getApplication(), false)
            PreferencesManager.saveDarkMode(getApplication(), false)
            _isDarkThemeCustom.value = false
        }
    }

    fun enableSystemMode(){
        viewModelScope.launch {
            PreferencesManager.saveUseSystemTheme(getApplication(), true)
        }
    }

}