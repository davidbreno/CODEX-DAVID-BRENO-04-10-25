package com.financeflow.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.financeflow.transactions.TransactionEntity
import com.financeflow.transactions.TransactionType
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun TransactionList(
    transactions: List<TransactionEntity>,
    onEdit: (TransactionEntity) -> Unit,
    onDelete: (TransactionEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(transactions) { transaction ->
            Surface(tonalElevation = 2.dp, shape = MaterialTheme.shapes.medium) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(text = transaction.title, style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = formatter.format(transaction.occurredAt.atZone(ZoneId.systemDefault()).toLocalDate()),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        text = formatAmount(transaction.amount, transaction.type),
                        color = if (transaction.type == TransactionType.INCOME) Color(0xFF4CAF50) else Color(0xFFF44336),
                        style = MaterialTheme.typography.titleMedium
                    )
                    IconButton(onClick = { onEdit(transaction) }) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { onDelete(transaction) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete")
                    }
                }
            }
        }
    }
}

private fun formatAmount(amount: Double, type: TransactionType): String {
    val sign = if (type == TransactionType.INCOME) "+" else "-"
    return "$sign$${"%.2f".format(amount)}"
}
