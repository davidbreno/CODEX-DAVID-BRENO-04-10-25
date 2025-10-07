package com.finacedavid.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.OffsetDateTime

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: TransactionType,
    val amount: BigDecimal,
    val timestamp: OffsetDateTime,
    val category: String,
    val description: String,
    val status: TransactionStatus,
    @ColumnInfo(name = "attachment_uri")
    val attachmentUri: String? = null,
    @ColumnInfo(name = "created_at")
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: OffsetDateTime = OffsetDateTime.now()
)

enum class TransactionType { Entrada, Saida }

enum class TransactionStatus { Pago, Pendente }
