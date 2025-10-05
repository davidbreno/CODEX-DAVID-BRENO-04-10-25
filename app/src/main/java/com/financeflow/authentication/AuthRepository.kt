package com.financeflow.authentication

import com.financeflow.settings.ThemeRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.security.MessageDigest

sealed class AuthState {
    object Unauthenticated : AuthState()
    data class Authenticated(val user: UserEntity) : AuthState()
    data class Error(val message: String) : AuthState()
    object Loading : AuthState()
}

class AuthRepository(
    private val userDao: UserDao,
    private val scope: CoroutineScope,
    private val themeRepository: ThemeRepository
) {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Unauthenticated)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        scope.launch(Dispatchers.IO) {
            _authState.value = AuthState.Loading
            val normalized = email.trim().lowercase()
            val existing = userDao.findByEmail(normalized)
            if (existing == null) {
                _authState.value = AuthState.Error("Account not found")
            } else if (existing.passwordHash == hash(password)) {
                _authState.value = AuthState.Authenticated(existing)
            } else {
                _authState.value = AuthState.Error("Invalid credentials")
            }
        }
    }

    fun register(email: String, password: String) {
        scope.launch(Dispatchers.IO) {
            _authState.value = AuthState.Loading
            val normalized = email.trim().lowercase()
            val existing = userDao.findByEmail(normalized)
            if (existing != null) {
                _authState.value = AuthState.Error("Email already registered")
                return@launch
            }
            val user = UserEntity(email = normalized, passwordHash = hash(password))
            val id = userDao.insert(user)
            _authState.value = AuthState.Authenticated(user.copy(id = id))
        }
    }

    fun logout() {
        _authState.value = AuthState.Unauthenticated
    }

    fun emitError(message: String) {
        _authState.value = AuthState.Error(message)
    }

    suspend fun selectThemeByName(name: String) {
        val gradient = com.financeflow.ui.theme.FinanceFlowPalettes.all.firstOrNull { it.name == name }
            ?: com.financeflow.ui.theme.FinanceFlowPalettes.Sunrise
        themeRepository.setTheme(gradient)
    }

    private fun hash(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(password.toByteArray())
        return bytes.joinToString(separator = "") { byte -> "%02x".format(byte) }
    }
}
