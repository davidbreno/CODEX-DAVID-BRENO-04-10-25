package com.financeflow.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.time.Instant

class TransactionViewModel(private val repository: TransactionRepository) : ViewModel() {
    val dashboard = repository.dashboard

    fun saveTransaction(
        id: Long?,
        title: String,
        amount: Double,
        type: TransactionType,
        category: String,
        occurredAt: Instant = Instant.now()
    ) {
        repository.upsertTransaction(id, title, amount, type, category, occurredAt)
    }

    fun toggleBillPaid(bill: BillEntity, paid: Boolean) {
        repository.markBillPaid(bill, paid)
    }

    fun deleteTransaction(transaction: TransactionEntity) {
        repository.deleteTransaction(transaction)
    }

    fun saveBill(id: Long?, name: String, amount: Double, dueDate: Instant, isPaid: Boolean) {
        repository.upsertBill(id, name, amount, dueDate, isPaid)
    }

    fun deleteBill(bill: BillEntity) {
        repository.deleteBill(bill)
    }

    class Factory(private val repository: TransactionRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return TransactionViewModel(repository) as T
        }
    }
}
