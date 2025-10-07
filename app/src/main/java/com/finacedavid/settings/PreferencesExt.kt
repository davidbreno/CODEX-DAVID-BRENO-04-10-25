package com.finacedavid.settings

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val STORE_NAME = "finace_david_preferences"

val Context.dataStore by preferencesDataStore(name = STORE_NAME)

class ThemeRepository(context: Context) {
    private val dataStore = context.dataStore
    private val darkModeKey = booleanPreferencesKey("dark_mode")

    val theme: Flow<Boolean> = dataStore.data.map { it[darkModeKey] ?: true }

    suspend fun setDarkMode(enabled: Boolean) {
        dataStore.edit { prefs ->
            prefs[darkModeKey] = enabled
        }
    }
}
