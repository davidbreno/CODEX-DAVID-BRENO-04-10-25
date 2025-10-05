package com.financeflow.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.financeflow.transactions.TransactionEntity
import com.financeflow.transactions.TransactionType
import java.time.Instant

@Composable
fun TransactionFormScreen(
    initial: TransactionEntity?,
    onSubmit: (Long?, String, Double, TransactionType, String, Instant) -> Unit,
    onCancel: () -> Unit
) {
    val titleState = remember { mutableStateOf(initial?.title ?: "") }
    val amountState = remember { mutableStateOf(initial?.amount?.toString() ?: "") }
    val categoryState = remember { mutableStateOf(initial?.category ?: "General") }
    val typeState = remember { mutableStateOf(initial?.type ?: TransactionType.EXPENSE) }

    Column(modifier = Modifier.padding(24.dp)) {
        Text(text = if (initial == null) "Add Transaction" else "Edit Transaction")
        OutlinedTextField(
            value = titleState.value,
            onValueChange = { titleState.value = it },
            label = { Text("Title") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        OutlinedTextField(
            value = amountState.value,
            onValueChange = { amountState.value = it },
            label = { Text("Amount") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        OutlinedTextField(
            value = categoryState.value,
            onValueChange = { categoryState.value = it },
            label = { Text("Category") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        Text(text = "Type", modifier = Modifier.padding(top = 16.dp))
        TypeSelector(selected = typeState.value, onSelect = { typeState.value = it })
        Button(
            onClick = {
                val amount = amountState.value.toDoubleOrNull() ?: 0.0
                val occurredAt = initial?.occurredAt ?: Instant.now()
                onSubmit(initial?.id, titleState.value, amount, typeState.value, categoryState.value, occurredAt)
            },
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth()
        ) {
            Text("Save")
        }
        TextButton(onClick = onCancel, modifier = Modifier.padding(top = 8.dp)) {
            Text("Cancel")
        }
    }
}

@Composable
private fun TypeSelector(selected: TransactionType, onSelect: (TransactionType) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 8.dp)) {
        RadioOption(label = "Income", value = TransactionType.INCOME, selected = selected, onSelect = onSelect)
        RadioOption(label = "Expense", value = TransactionType.EXPENSE, selected = selected, onSelect = onSelect)
    }
}

@Composable
private fun RadioOption(
    label: String,
    value: TransactionType,
    selected: TransactionType,
    onSelect: (TransactionType) -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RadioButton(selected = selected == value, onClick = { onSelect(value) })
        Text(text = label, modifier = Modifier.padding(start = 8.dp))
    }
}
