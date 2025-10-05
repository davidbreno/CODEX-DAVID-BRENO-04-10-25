package com.financeflow.analytics

import com.financeflow.transactions.TransactionEntity
import com.financeflow.transactions.TransactionType
import java.time.LocalDate
import java.time.ZoneId

data class ChartEntry(val date: LocalDate, val income: Double, val expense: Double)

data class AnalyticsSummary(
    val balance: Double,
    val income: Double,
    val expense: Double,
    val chart: List<ChartEntry>
)

object AnalyticsEngine {
    fun summarize(transactions: List<TransactionEntity>): AnalyticsSummary {
        val grouped = transactions.groupBy {
            it.occurredAt.atZone(ZoneId.systemDefault()).toLocalDate()
        }
        val chart = grouped.toSortedMap().map { (date, items) ->
            val income = items.filter { it.type == TransactionType.INCOME }.sumOf { it.amount }
            val expense = items.filter { it.type == TransactionType.EXPENSE }.sumOf { it.amount }
            ChartEntry(date, income, expense)
        }
        val incomeTotal = chart.sumOf { it.income }
        val expenseTotal = chart.sumOf { it.expense }
        return AnalyticsSummary(
            balance = incomeTotal - expenseTotal,
            income = incomeTotal,
            expense = expenseTotal,
            chart = chart
        )
    }
}
