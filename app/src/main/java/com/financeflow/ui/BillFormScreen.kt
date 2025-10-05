package com.financeflow.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
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
import com.financeflow.transactions.BillEntity
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun BillFormScreen(
    initial: BillEntity?,
    onSubmit: (Long?, String, Double, Instant, Boolean) -> Unit,
    onCancel: () -> Unit
) {
    val nameState = remember { mutableStateOf(initial?.name ?: "") }
    val amountState = remember { mutableStateOf(initial?.amount?.toString() ?: "") }
    val dueState = remember {
        mutableStateOf(
            initial?.dueDate?.atZone(ZoneId.systemDefault())?.toLocalDate()?.toString() ?: LocalDate.now().toString()
        )
    }
    val paidState = remember { mutableStateOf(initial?.isPaid ?: false) }

    Column(modifier = Modifier.padding(24.dp)) {
        Text(text = if (initial == null) "Add Bill" else "Edit Bill")
        OutlinedTextField(
            value = nameState.value,
            onValueChange = { nameState.value = it },
            label = { Text("Name") },
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
            value = dueState.value,
            onValueChange = { dueState.value = it },
            label = { Text("Due Date (YYYY-MM-DD)") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Checkbox(checked = paidState.value, onCheckedChange = { paidState.value = it })
            Text(text = "Mark as paid", modifier = Modifier.padding(start = 8.dp))
        }
        Button(
            onClick = {
                val amount = amountState.value.toDoubleOrNull() ?: 0.0
                val dueDate = runCatching { LocalDate.parse(dueState.value) }.getOrDefault(LocalDate.now())
                val instant = dueDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
                onSubmit(initial?.id, nameState.value, amount, instant, paidState.value)
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
