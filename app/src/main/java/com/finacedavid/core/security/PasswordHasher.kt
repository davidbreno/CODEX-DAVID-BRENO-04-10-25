package com.finacedavid.core.security

import android.util.Base64
import java.security.MessageDigest

object PasswordHasher {
    fun hash(raw: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(raw.toByteArray())
        return Base64.encodeToString(bytes, Base64.NO_WRAP)
    }
}
