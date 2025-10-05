package com.financeflow.transactions

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant

enum class TransactionType { INCOME, EXPENSE }

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "type") val type: TransactionType,
    @ColumnInfo(name = "category") val category: String,
    @ColumnInfo(name = "occurred_at") val occurredAt: Instant = Instant.now()
)

@Entity(tableName = "bills")
data class BillEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "due_date") val dueDate: Instant,
    @ColumnInfo(name = "amount") val amount: Double,
    @ColumnInfo(name = "is_paid") val isPaid: Boolean = false
)
