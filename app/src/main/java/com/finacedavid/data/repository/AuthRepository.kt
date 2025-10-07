package com.finacedavid.data.repository

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.finacedavid.core.security.PasswordHasher
import com.finacedavid.data.local.UserEntity
import com.finacedavid.data.local.dao.UserDao
import com.finacedavid.domain.model.UserCredentials
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class AuthRepository(
    private val context: Context,
    private val userDao: UserDao,
    private val applicationScope: CoroutineScope,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    private val prefs by lazy {
        val masterKey = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        EncryptedSharedPreferences.create(
            "finace_david_auth",
            masterKey,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun observeCredentials(): Flow<UserCredentials?> =
        userDao.observeUser().map { entity ->
            entity?.let {
                UserCredentials(
                    passwordHash = it.passwordHash,
                    isPin = prefs.getBoolean(KEY_IS_PIN, false)
                )
            }
        }

    suspend fun register(secret: String, isPin: Boolean) {
        val hash = PasswordHasher.hash(secret)
        val existing = userDao.getUser()
        if (existing == null) {
            userDao.insert(UserEntity(passwordHash = hash))
        } else {
            userDao.update(existing.copy(passwordHash = hash))
        }
        prefs.edit().putBoolean(KEY_IS_PIN, isPin).apply()
    }

    suspend fun login(secret: String): Boolean {
        val user = userDao.getUser() ?: return false
        val hash = PasswordHasher.hash(secret)
        return user.passwordHash == hash
    }

    suspend fun changeSecret(current: String, next: String, isPin: Boolean): Boolean {
        return if (login(current)) {
            register(next, isPin)
            true
        } else {
            false
        }
    }

    fun clearCredentials() {
        applicationScope.launch(dispatcher) {
            val user = userDao.getUser() ?: return@launch
            userDao.update(user.copy(passwordHash = ""))
            prefs.edit().clear().apply()
        }
    }

    companion object {
        private const val KEY_IS_PIN = "is_pin"
    }
}
