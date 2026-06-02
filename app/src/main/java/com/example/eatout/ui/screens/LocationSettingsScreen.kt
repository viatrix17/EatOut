package com.example.eatout.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eatout.ui.components.SettingsButton
import com.example.eatout.ui.components.SettingsDropdown
import com.example.eatout.viewmodel.MainViewModel

@Composable
fun LocationSettingsScreen(
    isTablet: Boolean,
    viewModel: MainViewModel
) {
    // TO DO pobieranie możliwych miast i krajów z api w Main View Model

    // MOCK DATA
    val city = "CITY_TEST"//by viewModel.selectedCity.collectAsState()
    val country = "COUNTRY_TEST"//by viewModel.selectedCountry.collectAsState()
    val isLocationEnabled = false //by viewModel.isLocationEnabled.collectAsState()

    val cities = listOf("Warszawa", "Kraków", "Berlin") // Przykładowe dane
    val countries = listOf("Poland", "Germany", "France")

    LocationSettingsPhoneLayout(
        currentCity = city,
        currentCountry = country,
        isLocationEnabled = isLocationEnabled,
        allCities = cities,
        allCountries = countries

    )
}

@Composable
fun LocationSettingsPhoneLayout(
    currentCity: String,
    currentCountry: String,
    isLocationEnabled: Boolean,
    allCities: List<String>,
    allCountries: List<String>
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ){
        SettingsDropdown(
            label = "City",
            currentValue = currentCity,
            options = allCities,
            onSelected = {/* TO DO zmiana w VM */}
        )
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
        SettingsDropdown(
            label = "Country",
            currentValue = currentCountry,
            options = allCountries,
            onSelected = {/* TO DO zmiana w VM */}
        )
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )

        SettingsButton(
            onClick = {/* TO DO wywołanie zmiany flagi */},
            label =  "Location access",
            value =  if (!isLocationEnabled) "Allow location access" else "Disable location access"
        )
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
    }
}
