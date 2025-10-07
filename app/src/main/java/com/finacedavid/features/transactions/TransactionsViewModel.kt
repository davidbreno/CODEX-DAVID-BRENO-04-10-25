package com.finacedavid.features.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.finacedavid.data.repository.TransactionRepository
import com.finacedavid.domain.model.PeriodFilter
import com.finacedavid.domain.model.Transaction
import com.finacedavid.domain.model.TransactionStatus
import com.finacedavid.domain.model.TransactionType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

sealed interface TransactionTab { val label: String }
object AllTab : TransactionTab { override val label = "Todas" }
object IncomeTab : TransactionTab { override val label = "Entradas" }
object OutcomeTab : TransactionTab { override val label = "Saídas" }

data class TransactionFilterState(
    val period: PeriodFilter = PeriodFilter.CurrentMonth,
    val category: String? = null,
    val query: String = "",
    val status: TransactionStatus? = null
)

data class TransactionsUiState(
    val transactions: List<Transaction> = emptyList(),
    val filtered: List<Transaction> = emptyList(),
    val tab: TransactionTab = AllTab,
    val filter: TransactionFilterState = TransactionFilterState(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class TransactionsViewModel(
    private val repository: TransactionRepository
) : ViewModel() {
    private val _state = MutableStateFlow(TransactionsUiState())
    val state: StateFlow<TransactionsUiState> = _state.asStateFlow()

    private val _editing = MutableStateFlow<Transaction?>(null)
    val editing: StateFlow<Transaction?> = _editing.asStateFlow()

    init {
        viewModelScope.launch {
            combine(repository.observeAll(), _state) { transactions, current ->
                val filtered = applyFilters(transactions, current.filter, current.tab)
                current.copy(transactions = transactions, filtered = filtered, isLoading = false)
            }.collect { uiState ->
                _state.value = uiState
            }
        }
    }

    fun updateTab(tab: TransactionTab) {
        _state.value = _state.value.copy(tab = tab)
    }

    fun setFilter(filter: TransactionFilterState) {
        _state.value = _state.value.copy(filter = filter)
    }

    fun edit(transaction: Transaction?) {
        _editing.value = transaction
    }

    private fun applyFilters(
        transactions: List<Transaction>,
        filter: TransactionFilterState,
        tab: TransactionTab
    ): List<Transaction> {
        val periodRange = filter.period.range()
        return transactions.filter { tx ->
            !tx.timestamp.isBefore(periodRange.first) && tx.timestamp.isBefore(periodRange.second)
        }.filter { tx ->
            when (tab) {
                IncomeTab -> tx.type == TransactionType.Entrada
                OutcomeTab -> tx.type == TransactionType.Saida
                else -> true
            }
        }.filter { tx ->
            filter.category?.let { category -> tx.category.equals(category, ignoreCase = true) } ?: true
        }.filter { tx ->
            filter.status?.let { status -> tx.status == status } ?: true
        }.filter { tx ->
            if (filter.query.isBlank()) true else {
                tx.description.contains(filter.query, ignoreCase = true) ||
                    tx.category.contains(filter.query, ignoreCase = true)
            }
        }.sortedByDescending { it.timestamp }
    }

    suspend fun save(transaction: Transaction) {
        repository.upsert(transaction)
        _editing.value = null
    }

    suspend fun delete(transaction: Transaction) {
        repository.delete(transaction)
    }

    suspend fun markStatus(transactionId: Long, status: TransactionStatus) {
        repository.updateStatus(transactionId, status)
    }

    companion object {
        fun factory(repository: TransactionRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return TransactionsViewModel(repository) as T
            }
        }
    }
}
