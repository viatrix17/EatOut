package com.example.eatout.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "settings")

object PreferencesManager {
    private val DARK_MODE_KEY = booleanPreferencesKey("is_dark_mode")
    private val USE_SYSTEM_THEME_KEY = booleanPreferencesKey("use_system_theme")

    suspend fun saveDarkMode(context: Context, isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = isDark
        }
    }

    fun getDarkMode(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[DARK_MODE_KEY] ?: false
        }
    }

    suspend fun saveUseSystemTheme(context: Context, useSystem: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USE_SYSTEM_THEME_KEY] = useSystem
        }

    }

    fun getUseSystemTheme(context: Context): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[USE_SYSTEM_THEME_KEY] ?: false
        }
    }


}