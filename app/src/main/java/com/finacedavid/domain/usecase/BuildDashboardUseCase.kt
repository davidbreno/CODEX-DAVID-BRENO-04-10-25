package com.finacedavid.domain.usecase

import androidx.compose.ui.graphics.Color
import com.finacedavid.domain.model.CalendarDaySummary
import com.finacedavid.domain.model.DashboardSummary
import com.finacedavid.domain.model.DonutSlice
import com.finacedavid.domain.model.PeriodFilter
import com.finacedavid.domain.model.Transaction
import com.finacedavid.domain.model.TransactionType
import com.finacedavid.domain.model.TrendPoint
import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.Locale
import kotlin.math.max

class BuildDashboardUseCase {
    private val locale = Locale("pt", "BR")
    private val incomeColor = Color(0xFF1ABCFE)
    private val outcomeColor = Color(0xFF1DA1F2)

    operator fun invoke(filter: PeriodFilter, transactions: List<Transaction>): DashboardSummary {
        val (start, end) = filter.range()
        val filtered = transactions.filter { tx ->
            !tx.timestamp.isBefore(start) && tx.timestamp.isBefore(end)
        }

        val totalIncome = filtered.filter { it.type == TransactionType.Entrada }
            .fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount }
        val totalOutcome = filtered.filter { it.type == TransactionType.Saida }
            .fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount }
        val balance = totalIncome - totalOutcome

        val donut = buildList {
            add(DonutSlice("Entradas", totalIncome, incomeColor.value.toLong()))
            add(DonutSlice("Saídas", totalOutcome, outcomeColor.value.toLong()))
        }

        val groupedByDay = filtered.groupBy { it.timestamp.atZoneSameInstant(ZoneId.systemDefault()).toLocalDate() }
        val days = generateSequence(start.toLocalDate()) { prev ->
            val next = prev.plusDays(1)
            if (next.isBefore(end.toLocalDate())) next else null
        }.toList() + end.toLocalDate()

        val trend = days.distinct().sorted().map { date ->
            val incomeOfDay = groupedByDay[date]?.filter { it.type == TransactionType.Entrada }
                ?.fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount } ?: BigDecimal.ZERO
            val outcomeOfDay = groupedByDay[date]?.filter { it.type == TransactionType.Saida }
                ?.fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount } ?: BigDecimal.ZERO
            TrendPoint(date, incomeOfDay - outcomeOfDay)
        }

        val calendar = groupedByDay.map { (date, entries) ->
            val total = entries.fold(BigDecimal.ZERO) { acc, tx -> acc + tx.amount }
            CalendarDaySummary(date, total, entries.isNotEmpty())
        }

        return DashboardSummary(
            balance = balance,
            totalIncome = totalIncome,
            totalOutcome = totalOutcome,
            donut = donut,
            trend = trend,
            calendar = calendar
        )
    }

    fun formatCurrency(value: BigDecimal): String = NumberFormat.getCurrencyInstance(locale).format(value)
}
