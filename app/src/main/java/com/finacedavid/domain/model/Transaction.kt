package com.finacedavid.domain.model

import java.math.BigDecimal
import java.time.OffsetDateTime

data class Transaction(
    val id: Long = 0,
    val type: TransactionType,
    val amount: BigDecimal,
    val timestamp: OffsetDateTime,
    val category: String,
    val description: String,
    val status: TransactionStatus,
    val attachmentUri: String?
)

enum class TransactionType { Entrada, Saida }

enum class TransactionStatus { Pago, Pendente }
