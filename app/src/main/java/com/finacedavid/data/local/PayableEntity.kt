package com.finacedavid.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime

@Entity(tableName = "payables")
data class PayableEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val amount: BigDecimal,
    val dueDate: LocalDate,
    val status: PayableStatus,
    val note: String?,
    @ColumnInfo(name = "created_at")
    val createdAt: OffsetDateTime = OffsetDateTime.now(),
    @ColumnInfo(name = "updated_at")
    val updatedAt: OffsetDateTime = OffsetDateTime.now()
)

enum class PayableStatus { Pendente, Pago }
