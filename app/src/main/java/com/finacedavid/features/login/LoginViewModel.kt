package com.finacedavid.features.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.finacedavid.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed interface LoginUiState {
    data object Loading : LoginUiState
    data class Setup(val error: String? = null) : LoginUiState
    data class Login(val isPin: Boolean, val error: String? = null) : LoginUiState
    data object Authenticated : LoginUiState
}

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    private val _state = MutableStateFlow<LoginUiState>(LoginUiState.Loading)
    val state: StateFlow<LoginUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.observeCredentials().collect { credentials ->
                _state.value = if (credentials == null || credentials.passwordHash.isBlank()) {
                    LoginUiState.Setup()
                } else {
                    LoginUiState.Login(isPin = credentials.isPin)
                }
            }
        }
    }

    fun register(secret: String, isPin: Boolean) {
        viewModelScope.launch {
            try {
                authRepository.register(secret, isPin)
                _state.value = LoginUiState.Authenticated
            } catch (ex: Exception) {
                _state.value = LoginUiState.Setup(error = ex.message)
            }
        }
    }

    fun authenticate(secret: String) {
        viewModelScope.launch {
            val success = authRepository.login(secret)
            if (success) {
                _state.value = LoginUiState.Authenticated
            } else {
                val current = _state.value
                if (current is LoginUiState.Login) {
                    _state.value = current.copy(error = "Credenciais inválidas")
                }
            }
        }
    }

    fun reset() {
        _state.value = LoginUiState.Setup()
    }

    companion object {
        fun factory(authRepository: AuthRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return LoginViewModel(authRepository) as T
            }
        }
    }
}
