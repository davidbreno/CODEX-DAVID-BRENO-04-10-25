package com.financeflow.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.financeflow.ui.theme.FinanceFlowPalettes
import com.financeflow.ui.theme.FinanceFlowGradient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ThemeRepository(private val dataStore: DataStore<Preferences>) {
    private val themeKey = stringPreferencesKey("selected_theme")

    val theme: Flow<FinanceFlowGradient> = dataStore.data.map { preferences ->
        val name = preferences[themeKey]
        FinanceFlowPalettes.all.find { it.name == name } ?: FinanceFlowPalettes.Sunrise
    }

    suspend fun setTheme(gradient: FinanceFlowGradient) {
        dataStore.edit { prefs ->
            prefs[themeKey] = gradient.name
        }
    }
}
