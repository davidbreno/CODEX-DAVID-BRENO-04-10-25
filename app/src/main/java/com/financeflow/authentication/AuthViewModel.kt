package com.financeflow.authentication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    val authState: StateFlow<AuthState> = repository.authState

    fun login(email: String, password: String) {
        repository.login(email, password)
    }

    fun register(email: String, password: String, confirm: String) {
        if (password != confirm) {
            repository.emitError("Passwords do not match")
            return
        }
        repository.register(email, password)
    }

    fun logout() = repository.logout()

    fun selectTheme(name: String) {
        viewModelScope.launch {
            repository.selectThemeByName(name)
        }
    }

    class Factory(private val repository: AuthRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthViewModel(repository) as T
        }
    }
}
