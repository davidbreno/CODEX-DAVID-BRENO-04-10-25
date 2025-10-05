package com.financeflow.transactions

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant

data class DashboardSummary(
    val transactions: List<TransactionEntity> = emptyList(),
    val incomeTotal: Double = 0.0,
    val expenseTotal: Double = 0.0,
    val bills: List<BillEntity> = emptyList()
) {
    val balance: Double get() = incomeTotal - expenseTotal
}

class TransactionRepository(
    private val transactionDao: TransactionDao,
    private val billDao: BillDao,
    private val scope: CoroutineScope
) {
    val dashboard: StateFlow<DashboardSummary> = combine(
        transactionDao.observeTransactions(),
        transactionDao.observeTotalIncome(),
        transactionDao.observeTotalExpense(),
        billDao.observeUpcomingBills()
    ) { transactions, income, expense, bills ->
        DashboardSummary(
            transactions = transactions,
            incomeTotal = income ?: 0.0,
            expenseTotal = expense ?: 0.0,
            bills = bills
        )
    }.stateIn(scope, SharingStarted.WhileSubscribed(5_000), DashboardSummary())

    fun upsertTransaction(
        id: Long?,
        title: String,
        amount: Double,
        type: TransactionType,
        category: String,
        occurredAt: Instant = Instant.now()
    ) {
        scope.launch(Dispatchers.IO) {
            val entity = TransactionEntity(
                id = id ?: 0,
                title = title,
                amount = amount,
                type = type,
                category = category,
                occurredAt = occurredAt
            )
            transactionDao.upsert(entity)
        }
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        scope.launch(Dispatchers.IO) {
            transactionDao.delete(transaction)
        }
    }

    fun upsertBill(id: Long?, name: String, amount: Double, dueDate: Instant, isPaid: Boolean) {
        scope.launch(Dispatchers.IO) {
            val bill = BillEntity(
                id = id ?: 0,
                name = name,
                amount = amount,
                dueDate = dueDate,
                isPaid = isPaid
            )
            billDao.upsert(bill)
        }
    }

    fun markBillPaid(bill: BillEntity, paid: Boolean) {
        scope.launch(Dispatchers.IO) {
            billDao.update(bill.copy(isPaid = paid))
        }
    }

    fun deleteBill(bill: BillEntity) {
        scope.launch(Dispatchers.IO) {
            billDao.delete(bill)
        }
    }
}
