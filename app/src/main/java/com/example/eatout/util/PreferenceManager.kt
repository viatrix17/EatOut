package com.example.eatout.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")


// PreferencesManager.kt
class PreferencesManager(private val context: Context) {

    private val DARK_MODE_KEY = booleanPreferencesKey("is_dark_mode")
    private val USE_SYSTEM_THEME_KEY = booleanPreferencesKey("use_system_theme")

    suspend fun saveDarkMode(isDark: Boolean) {
        context.dataStore.edit { it[DARK_MODE_KEY] = isDark }
    }

    fun getDarkMode(): Flow<Boolean> {
        return context.dataStore.data.map { it[DARK_MODE_KEY] ?: false }
    }

    suspend fun saveUseSystemTheme(useSystem: Boolean) {
        context.dataStore.edit { it[USE_SYSTEM_THEME_KEY] = useSystem }
    }

    fun getUseSystemTheme(): Flow<Boolean> {
        return context.dataStore.data.map { it[USE_SYSTEM_THEME_KEY] ?: true }
    }
}