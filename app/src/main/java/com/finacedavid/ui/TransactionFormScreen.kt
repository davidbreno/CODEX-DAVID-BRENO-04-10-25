package com.finacedavid.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.finacedavid.domain.model.Transaction
import com.finacedavid.domain.model.TransactionStatus
import com.finacedavid.domain.model.TransactionType
import java.math.BigDecimal
import java.time.OffsetDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionFormScreen(
    initial: Transaction?,
    onSubmit: (Transaction) -> Unit
) {
    val amount = remember { mutableStateOf(initial?.amount?.toPlainString() ?: "") }
    val category = remember { mutableStateOf(initial?.category ?: "") }
    val description = remember { mutableStateOf(initial?.description ?: "") }
    val typeExpanded = remember { mutableStateOf(false) }
    val statusExpanded = remember { mutableStateOf(false) }
    val type = remember { mutableStateOf(initial?.type ?: TransactionType.Entrada) }
    val status = remember { mutableStateOf(initial?.status ?: TransactionStatus.Pago) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = if (initial == null) "Nova Transação" else "Editar Transação", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = amount.value,
            onValueChange = { amount.value = it },
            label = { Text("Valor") },
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = category.value,
            onValueChange = { category.value = it },
            label = { Text("Categoria") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description.value,
            onValueChange = { description.value = it },
            label = { Text("Descrição") },
            modifier = Modifier.fillMaxWidth()
        )
        ExposedDropdownMenuBox(
            expanded = typeExpanded.value,
            onExpandedChange = { typeExpanded.value = !typeExpanded.value }
        ) {
            OutlinedTextField(
                value = type.value.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipo") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded.value) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            DropdownMenu(expanded = typeExpanded.value, onDismissRequest = { typeExpanded.value = false }) {
                TransactionType.values().forEach { option ->
                    DropdownMenuItem(text = { Text(option.name) }, onClick = {
                        type.value = option
                        typeExpanded.value = false
                    })
                }
            }
        }
        ExposedDropdownMenuBox(
            expanded = statusExpanded.value,
            onExpandedChange = { statusExpanded.value = !statusExpanded.value }
        ) {
            OutlinedTextField(
                value = status.value.name,
                onValueChange = {},
                readOnly = true,
                label = { Text("Status") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded.value) },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            DropdownMenu(expanded = statusExpanded.value, onDismissRequest = { statusExpanded.value = false }) {
                TransactionStatus.values().forEach { option ->
                    DropdownMenuItem(text = { Text(option.name) }, onClick = {
                        status.value = option
                        statusExpanded.value = false
                    })
                }
            }
        }
        Button(
            onClick = {
                val transaction = Transaction(
                    id = initial?.id ?: 0,
                    type = type.value,
                    amount = amount.value.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                    timestamp = initial?.timestamp ?: OffsetDateTime.now(),
                    category = category.value,
                    description = description.value,
                    status = status.value,
                    attachmentUri = initial?.attachmentUri
                )
                onSubmit(transaction)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Salvar")
        }
    }
}
