package com.finacedavid.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.finacedavid.domain.model.Transaction
import com.finacedavid.features.transactions.AllTab
import com.finacedavid.features.transactions.IncomeTab
import com.finacedavid.features.transactions.OutcomeTab
import com.finacedavid.features.transactions.TransactionFilterState
import com.finacedavid.features.transactions.TransactionTab
import com.finacedavid.features.transactions.TransactionsUiState
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TransactionsScreen(
    state: TransactionsUiState,
    onTabSelected: (TransactionTab) -> Unit,
    onFilterChanged: (TransactionFilterState) -> Unit,
    onCreate: () -> Unit,
    onOpen: (Transaction) -> Unit,
    formatCurrency: (java.math.BigDecimal) -> String
) {
    val locale = Locale("pt", "BR")
    val dateFormatter = remember { DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm", locale) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TabChip("Todas", state.tab is AllTab) { onTabSelected(AllTab) }
            TabChip("Entradas", state.tab is IncomeTab) { onTabSelected(IncomeTab) }
            TabChip("Saídas", state.tab is OutcomeTab) { onTabSelected(OutcomeTab) }
        }
        OutlinedTextField(
            value = state.filter.query,
            onValueChange = { query ->
                onFilterChanged(state.filter.copy(query = query))
            },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Buscar") }
        )
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            item {
                TextButton(onClick = onCreate, modifier = Modifier.fillMaxWidth()) {
                    Text("Nova Transação")
                }
            }
            items(state.filtered) { transaction ->
                TransactionItem(transaction, dateFormatter, formatCurrency, onOpen)
            }
        }
    }
}

@Composable
private fun TabChip(text: String, selected: Boolean, onClick: () -> Unit) {
    AssistChip(
        onClick = onClick,
        label = { Text(text) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface,
            labelColor = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface
        )
    )
}

@Composable
private fun TransactionItem(
    transaction: Transaction,
    formatter: DateTimeFormatter,
    formatCurrency: (java.math.BigDecimal) -> String,
    onOpen: (Transaction) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithoutRipple { onOpen(transaction) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(transaction.category, fontWeight = FontWeight.Bold)
                Text(formatCurrency(transaction.amount))
            }
            Text(transaction.description)
            Text(formatter.format(transaction.timestamp))
            Text(transaction.status.name)
        }
    }
}
