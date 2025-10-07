package com.finacedavid.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.finacedavid.settings.ThemeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val themeRepository: ThemeRepository
) : ViewModel() {
    private val _isDark = MutableStateFlow(true)
    val isDark: StateFlow<Boolean> = _isDark.asStateFlow()

    init {
        viewModelScope.launch {
            themeRepository.theme.collect { isDark ->
                _isDark.value = isDark
            }
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            themeRepository.setDarkMode(!_isDark.value)
        }
    }

    companion object {
        fun factory(themeRepository: ThemeRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SettingsViewModel(themeRepository) as T
            }
        }
    }
}
