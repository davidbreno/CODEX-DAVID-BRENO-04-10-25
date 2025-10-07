package com.finacedavid.domain.model

data class UserCredentials(
    val passwordHash: String,
    val isPin: Boolean
)
