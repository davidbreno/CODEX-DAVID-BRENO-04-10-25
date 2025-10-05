package com.financeflow.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.financeflow.transactions.BillEntity
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun BillList(
    bills: List<BillEntity>,
    onTogglePaid: (BillEntity, Boolean) -> Unit,
    onEdit: (BillEntity) -> Unit,
    onDelete: (BillEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("MMM dd")
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        bills.forEach { bill ->
            Surface(
                tonalElevation = 2.dp,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = bill.name, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Due ${formatter.format(bill.dueDate.atZone(ZoneId.systemDefault()).toLocalDate())}",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        text = "$${"%.2f".format(bill.amount)}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Checkbox(checked = bill.isPaid, onCheckedChange = { paid -> onTogglePaid(bill, paid) })
                    IconButton(onClick = { onEdit(bill) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit Bill")
                    }
                    IconButton(onClick = { onDelete(bill) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete Bill")
                    }
                }
            }
        }
    }
}
