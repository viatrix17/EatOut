package com.example.eatout.util

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
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

    private val LAST_RESET_DATE_KEY = stringPreferencesKey("last_reset_date")

    suspend fun saveLastResetDate(date: String) {
        context.dataStore.edit { it[LAST_RESET_DATE_KEY] = date }
    }

    val lastResetDateFlow: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[LAST_RESET_DATE_KEY] ?: ""
    }
    private val REC_DATE_KEY = stringPreferencesKey("rec_date")
    private val REC_ID_KEY = longPreferencesKey("rec_id")
    private val REC_NAME_KEY = stringPreferencesKey("rec_name")

    suspend fun saveRecommendation(date: String, id: Long, name: String) {
        context.dataStore.edit {
            it[REC_DATE_KEY] = date
            it[REC_ID_KEY] = id
            it[REC_NAME_KEY] = name
        }
    }

    data class SavedRecommendation(val date: String, val id: Long, val name: String)

    val recFlow: Flow<SavedRecommendation?> = context.dataStore.data.map { prefs ->
        val date = prefs[REC_DATE_KEY] ?: return@map null
        val id = prefs[REC_ID_KEY] ?: return@map null
        val name = prefs[REC_NAME_KEY] ?: return@map null
        SavedRecommendation(date, id, name)
    }

}