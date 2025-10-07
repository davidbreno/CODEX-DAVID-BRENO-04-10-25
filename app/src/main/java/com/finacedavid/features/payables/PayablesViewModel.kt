package com.finacedavid.features.payables

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.finacedavid.data.repository.PayableRepository
import com.finacedavid.domain.model.Payable
import com.finacedavid.domain.model.PayableStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

sealed class PayablesSection(val title: String) {
    object Overdue : PayablesSection("Vencidas")
    object Today : PayablesSection("Hoje")
    object Upcoming : PayablesSection("Próximas")
}

data class PayablesUiState(
    val overdue: List<Payable> = emptyList(),
    val today: List<Payable> = emptyList(),
    val upcoming: List<Payable> = emptyList(),
    val isLoading: Boolean = true
)

class PayablesViewModel(
    private val repository: PayableRepository
) : ViewModel() {
    private val _state = MutableStateFlow(PayablesUiState())
    val state: StateFlow<PayablesUiState> = _state.asStateFlow()

    private val _editing = MutableStateFlow<Payable?>(null)
    val editing: StateFlow<Payable?> = _editing.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeAll().collect { payables ->
                val todayDate = LocalDate.now()
                _state.value = PayablesUiState(
                    overdue = payables.filter { it.dueDate.isBefore(todayDate) && it.status == PayableStatus.Pendente },
                    today = payables.filter { it.dueDate == todayDate },
                    upcoming = payables.filter { it.dueDate.isAfter(todayDate) },
                    isLoading = false
                )
            }
        }
    }

    fun edit(payable: Payable?) {
        _editing.value = payable
    }

    suspend fun save(payable: Payable) {
        repository.upsert(payable)
        _editing.value = null
    }

    suspend fun delete(payable: Payable) {
        repository.delete(payable)
    }

    suspend fun markPaid(payable: Payable) {
        repository.markStatus(payable.id, PayableStatus.Pago)
    }

    companion object {
        fun factory(repository: PayableRepository) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PayablesViewModel(repository) as T
            }
        }
    }
}
