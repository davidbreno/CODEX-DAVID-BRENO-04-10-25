package com.finacedavid

import com.finacedavid.domain.model.PeriodFilter
import com.finacedavid.domain.model.Transaction
import com.finacedavid.domain.model.TransactionStatus
import com.finacedavid.domain.model.TransactionType
import com.finacedavid.domain.usecase.BuildDashboardUseCase
import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal
import java.time.OffsetDateTime

class BuildDashboardUseCaseTest {
    private val useCase = BuildDashboardUseCase()

    @Test
    fun `should compute totals for current month`() {
        val now = OffsetDateTime.now()
        val transactions = listOf(
            Transaction(
                id = 1,
                type = TransactionType.Entrada,
                amount = BigDecimal("100"),
                timestamp = now.minusDays(1),
                category = "Salário",
                description = "Pagamento",
                status = TransactionStatus.Pago,
                attachmentUri = null
            ),
            Transaction(
                id = 2,
                type = TransactionType.Saida,
                amount = BigDecimal("40"),
                timestamp = now.minusDays(1),
                category = "Mercado",
                description = "Compras",
                status = TransactionStatus.Pago,
                attachmentUri = null
            )
        )

        val summary = useCase(PeriodFilter.CurrentMonth, transactions)

        assertEquals(BigDecimal("60"), summary.balance)
        assertEquals(BigDecimal("100"), summary.totalIncome)
        assertEquals(BigDecimal("40"), summary.totalOutcome)
        assertEquals(2, summary.donut.size)
        assertEquals(2, summary.trend.size)
    }
}
