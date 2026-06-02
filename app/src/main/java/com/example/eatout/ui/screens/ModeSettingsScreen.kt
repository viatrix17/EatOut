package com.example.eatout.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.eatout.ui.components.SettingsButton
import com.example.eatout.ui.components.WideButton
import com.example.eatout.viewmodel.MainViewModel

@Composable
fun ModeSettingsScreen(
    isTablet: Boolean,
    viewModel: MainViewModel
) {
    val isDarkMode = false //by viewModel.isDarkMode.collectAsState()

    ModeSettingsPhoneLayout()
}

@Composable
fun ModeSettingsPhoneLayout(){
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ){
        WideButton(
            onClick = {/* TO DO zmiana na tryb jasny */},
            label =  "Light mode"
        )
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
        WideButton(
            onClick = {/* TO DO zmiana na tryb ciemny */},
            label =  "Dark mode"
        )
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
        WideButton(
            onClick = {/* TO DO zmiana na zależne od ustawień telefonu */},
            label =  "Use system settings"
        )
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
    }
}