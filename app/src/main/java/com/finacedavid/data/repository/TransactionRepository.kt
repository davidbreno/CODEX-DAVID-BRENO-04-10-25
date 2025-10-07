package com.finacedavid.data.repository

import com.finacedavid.data.local.TransactionEntity
import com.finacedavid.data.local.TransactionStatus as EntityStatus
import com.finacedavid.data.local.TransactionType as EntityType
import com.finacedavid.data.local.dao.TransactionDao
import com.finacedavid.domain.mappers.toDomain
import com.finacedavid.domain.mappers.toEntity
import com.finacedavid.domain.model.Transaction
import com.finacedavid.domain.model.TransactionStatus
import com.finacedavid.domain.model.TransactionType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.time.LocalDate
import java.time.OffsetDateTime

class TransactionRepository(
    private val dao: TransactionDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun observeAll(): Flow<List<Transaction>> = dao.observeAll().map { it.map(TransactionEntity::toDomain) }

    fun observeByType(type: TransactionType): Flow<List<Transaction>> =
        dao.observeByType(type.toEntityType()).map { entities -> entities.map(TransactionEntity::toDomain) }

    suspend fun getBetween(start: OffsetDateTime, end: OffsetDateTime): List<Transaction> =
        withContext(dispatcher) { dao.findBetween(start, end).map(TransactionEntity::toDomain) }

    suspend fun upsert(transaction: Transaction): Long = withContext(dispatcher) {
        if (transaction.id == 0L) {
            dao.insert(transaction.toEntity())
        } else {
            dao.update(transaction.toEntity())
            transaction.id
        }
    }

    suspend fun delete(transaction: Transaction) = withContext(dispatcher) {
        dao.delete(transaction.toEntity())
    }

    suspend fun updateStatus(id: Long, status: TransactionStatus) = withContext(dispatcher) {
        dao.updateStatus(id, status.toEntityStatus(), OffsetDateTime.now())
    }

    suspend fun calculateTotals(
        transactions: List<Transaction>
    ): Triple<BigDecimal, BigDecimal, BigDecimal> {
        var income = BigDecimal.ZERO
        var outcome = BigDecimal.ZERO
        transactions.forEach { tx ->
            when (tx.type) {
                TransactionType.Entrada -> income += tx.amount
                TransactionType.Saida -> outcome += tx.amount
            }
        }
        val balance = income - outcome
        return Triple(balance, income, outcome)
    }

    private fun TransactionType.toEntityType(): EntityType = when (this) {
        TransactionType.Entrada -> EntityType.Entrada
        TransactionType.Saida -> EntityType.Saida
    }

    private fun TransactionStatus.toEntityStatus(): EntityStatus = when (this) {
        TransactionStatus.Pago -> EntityStatus.Pago
        TransactionStatus.Pendente -> EntityStatus.Pendente
    }
}
