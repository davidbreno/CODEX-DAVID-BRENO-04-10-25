package com.finacedavid.data.repository

import com.finacedavid.data.local.PayableEntity
import com.finacedavid.data.local.PayableStatus as EntityStatus
import com.finacedavid.data.local.dao.PayableDao
import com.finacedavid.domain.mappers.toDomain
import com.finacedavid.domain.mappers.toEntity
import com.finacedavid.domain.model.Payable
import com.finacedavid.domain.model.PayableStatus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDate

class PayableRepository(
    private val dao: PayableDao,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    fun observeAll(): Flow<List<Payable>> = dao.observeAll().map { it.map(PayableEntity::toDomain) }

    suspend fun getOverdue(today: LocalDate): List<Payable> =
        withContext(dispatcher) { dao.getOverdue(today).map(PayableEntity::toDomain) }

    suspend fun upsert(payable: Payable): Long = withContext(dispatcher) {
        if (payable.id == 0L) {
            dao.insert(payable.toEntity())
        } else {
            dao.update(payable.toEntity())
            payable.id
        }
    }

    suspend fun delete(payable: Payable) = withContext(dispatcher) {
        dao.delete(payable.toEntity())
    }

    suspend fun markStatus(id: Long, status: PayableStatus) = withContext(dispatcher) {
        dao.updateStatus(id, when (status) {
            PayableStatus.Pendente -> EntityStatus.Pendente
            PayableStatus.Pago -> EntityStatus.Pago
        })
    }
}
