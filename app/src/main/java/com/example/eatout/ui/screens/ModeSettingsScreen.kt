package com.example.eatout.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eatout.ui.components.WideButton
import com.example.eatout.viewmodel.MainViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun ModeSettingsScreen(
    isTablet: Boolean,
    viewModel: MainViewModel,
) {
    val isDarkTheme by viewModel.isDarkThemeCustom.collectAsState()

    ModeSettingsPhoneLayout(
        onLightModeClick = { viewModel.changeToLightMode() },
        onDarkModeClick = { viewModel.changeToDarkMode() },
        onSystemModeClick = { viewModel.enableSystemMode() }
    )
}

@Composable
fun ModeSettingsPhoneLayout(
    onLightModeClick: () -> Unit,
    onDarkModeClick: () -> Unit,
    onSystemModeClick: () -> Unit
){
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ){
        WideButton(
            onClick = onLightModeClick,
            label =  "Light mode"
        )
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
        WideButton(
            onClick = onDarkModeClick,
            label =  "Dark mode"
        )
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
        WideButton(
            onClick = onSystemModeClick,
            label =  "Use system settings"
        )
        HorizontalDivider(
            thickness = 0.5.dp,
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
        )
    }
}