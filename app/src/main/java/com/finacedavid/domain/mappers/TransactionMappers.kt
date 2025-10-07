package com.finacedavid.domain.mappers

import com.finacedavid.data.local.TransactionEntity
import com.finacedavid.data.local.TransactionStatus as EntityStatus
import com.finacedavid.data.local.TransactionType as EntityType
import com.finacedavid.domain.model.Transaction
import com.finacedavid.domain.model.TransactionStatus
import com.finacedavid.domain.model.TransactionType

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id = id,
    type = when (type) {
        EntityType.Entrada -> TransactionType.Entrada
        EntityType.Saida -> TransactionType.Saida
    },
    amount = amount,
    timestamp = timestamp,
    category = category,
    description = description,
    status = when (status) {
        EntityStatus.Pago -> TransactionStatus.Pago
        EntityStatus.Pendente -> TransactionStatus.Pendente
    },
    attachmentUri = attachmentUri
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    type = when (type) {
        TransactionType.Entrada -> EntityType.Entrada
        TransactionType.Saida -> EntityType.Saida
    },
    amount = amount,
    timestamp = timestamp,
    category = category,
    description = description,
    status = when (status) {
        TransactionStatus.Pago -> EntityStatus.Pago
        TransactionStatus.Pendente -> EntityStatus.Pendente
    },
    attachmentUri = attachmentUri
)
