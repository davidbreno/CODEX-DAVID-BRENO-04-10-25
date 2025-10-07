package com.finacedavid.features.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.finacedavid.data.repository.PayableRepository
import com.finacedavid.data.repository.TransactionRepository
import com.finacedavid.domain.model.DashboardSummary
import com.finacedavid.domain.model.PayableStatus
import com.finacedavid.domain.model.PeriodFilter
import com.finacedavid.domain.model.Transaction
import com.finacedavid.domain.usecase.BuildDashboardUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Loaded(
        val filter: PeriodFilter,
        val summary: DashboardSummary,
        val transactions: List<Transaction>,
        val payablesCount: Int
    ) : HomeUiState
}

class HomeViewModel(
    private val transactionRepository: TransactionRepository,
    private val payableRepository: PayableRepository,
    private val buildDashboardUseCase: BuildDashboardUseCase
) : ViewModel() {
    private val _filter = MutableStateFlow<PeriodFilter>(PeriodFilter.CurrentMonth)
    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                transactionRepository.observeAll(),
                payableRepository.observeAll(),
                _filter
            ) { transactions, payables, filter ->
                val summary = buildDashboardUseCase(filter, transactions)
                HomeUiState.Loaded(
                    filter = filter,
                    summary = summary,
                    transactions = transactions,
                    payablesCount = payables.count { it.status == PayableStatus.Pendente }
                )
            }.collect { uiState ->
                _state.value = uiState
            }
        }
    }

    fun updateFilter(filter: PeriodFilter) {
        _filter.value = filter
    }

    fun formatCurrency(value: java.math.BigDecimal): String = buildDashboardUseCase.formatCurrency(value)

    companion object {
        fun factory(
            transactionRepository: TransactionRepository,
            payableRepository: PayableRepository
        ) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(
                    transactionRepository,
                    payableRepository,
                    BuildDashboardUseCase()
                ) as T
            }
        }
    }
}
