package com.finacedavid.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.finacedavid.domain.model.Payable
import com.finacedavid.domain.model.PayableStatus
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun PayableFormScreen(
    initial: Payable?,
    onSubmit: (Payable) -> Unit
) {
    val title = remember { mutableStateOf(initial?.title ?: "") }
    val amount = remember { mutableStateOf(initial?.amount?.toPlainString() ?: "") }
    val dueDate = remember { mutableStateOf(initial?.dueDate ?: LocalDate.now()) }
    val note = remember { mutableStateOf(initial?.note ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(if (initial == null) "Nova Conta" else "Editar Conta", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = title.value,
            onValueChange = { title.value = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = amount.value,
            onValueChange = { amount.value = it },
            label = { Text("Valor") },
            keyboardOptions = androidx.compose.ui.text.input.KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = dueDate.value.toString(),
            onValueChange = { value ->
                runCatching { LocalDate.parse(value) }.getOrNull()?.let { dueDate.value = it }
            },
            label = { Text("Vencimento (AAAA-MM-DD)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = note.value,
            onValueChange = { note.value = it },
            label = { Text("Nota") },
            modifier = Modifier.fillMaxWidth()
        )
        Button(onClick = {
            val payable = Payable(
                id = initial?.id ?: 0,
                title = title.value,
                amount = amount.value.toBigDecimalOrNull() ?: BigDecimal.ZERO,
                dueDate = dueDate.value,
                status = initial?.status ?: PayableStatus.Pendente,
                note = note.value
            )
            onSubmit(payable)
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Salvar")
        }
    }
}
