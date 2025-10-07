package com.finacedavid.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class Payable(
    val id: Long = 0,
    val title: String,
    val amount: BigDecimal,
    val dueDate: LocalDate,
    val status: PayableStatus,
    val note: String?
)

enum class PayableStatus { Pendente, Pago }
