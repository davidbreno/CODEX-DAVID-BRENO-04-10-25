package com.finacedavid.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.finacedavid.domain.model.Payable
import com.finacedavid.domain.model.PayableStatus
import com.finacedavid.features.payables.PayablesUiState
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun PayablesScreen(
    state: PayablesUiState,
    onCreate: () -> Unit,
    onOpen: (Payable) -> Unit,
    onMarkPaid: (Payable) -> Unit,
    formatCurrency: (java.math.BigDecimal) -> String
) {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("pt", "BR"))
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            TextButton(onClick = onCreate, modifier = Modifier.fillMaxWidth()) {
                Text("Nova Conta a Pagar")
            }
        }
        item {
            Text("Vencidas", style = MaterialTheme.typography.titleMedium)
        }
        items(state.overdue) { payable ->
            PayableItem(payable, formatter, formatCurrency, onOpen, onMarkPaid)
        }
        item { Text("Hoje", style = MaterialTheme.typography.titleMedium) }
        items(state.today) { payable ->
            PayableItem(payable, formatter, formatCurrency, onOpen, onMarkPaid)
        }
        item { Text("Próximas", style = MaterialTheme.typography.titleMedium) }
        items(state.upcoming) { payable ->
            PayableItem(payable, formatter, formatCurrency, onOpen, onMarkPaid)
        }
    }
}

@Composable
private fun PayableItem(
    payable: Payable,
    formatter: DateTimeFormatter,
    formatCurrency: (java.math.BigDecimal) -> String,
    onOpen: (Payable) -> Unit,
    onMarkPaid: (Payable) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithoutRipple { onOpen(payable) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(payable.title, fontWeight = FontWeight.Bold)
            Text(formatCurrency(payable.amount))
            Text(formatter.format(payable.dueDate))
            if (payable.status == PayableStatus.Pendente) {
                TextButton(onClick = { onMarkPaid(payable) }) {
                    Text("Marcar como Paga")
                }
            }
        }
    }
}
