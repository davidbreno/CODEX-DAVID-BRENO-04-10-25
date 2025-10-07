package com.finacedavid.domain.model

import java.math.BigDecimal
import java.time.LocalDate

sealed interface PeriodRange {
    val label: String

    data class Preset(override val label: String, val days: Long) : PeriodRange
    data class Custom(override val label: String, val start: LocalDate, val end: LocalDate) : PeriodRange
}

data class DonutSlice(
    val label: String,
    val value: BigDecimal,
    val color: Long
)

data class TrendPoint(
    val date: LocalDate,
    val balance: BigDecimal
)

data class CalendarDaySummary(
    val date: LocalDate,
    val total: BigDecimal,
    val hasEntries: Boolean
)

data class DashboardSummary(
    val balance: BigDecimal,
    val totalIncome: BigDecimal,
    val totalOutcome: BigDecimal,
    val donut: List<DonutSlice>,
    val trend: List<TrendPoint>,
    val calendar: List<CalendarDaySummary>
)
