package com.finacedavid.domain.mappers

import com.finacedavid.data.local.PayableEntity
import com.finacedavid.data.local.PayableStatus as EntityStatus
import com.finacedavid.domain.model.Payable
import com.finacedavid.domain.model.PayableStatus

fun PayableEntity.toDomain(): Payable = Payable(
    id = id,
    title = title,
    amount = amount,
    dueDate = dueDate,
    status = when (status) {
        EntityStatus.Pendente -> PayableStatus.Pendente
        EntityStatus.Pago -> PayableStatus.Pago
    },
    note = note
)

fun Payable.toEntity(): PayableEntity = PayableEntity(
    id = id,
    title = title,
    amount = amount,
    dueDate = dueDate,
    status = when (status) {
        PayableStatus.Pendente -> EntityStatus.Pendente
        PayableStatus.Pago -> EntityStatus.Pago
    },
    note = note
)
