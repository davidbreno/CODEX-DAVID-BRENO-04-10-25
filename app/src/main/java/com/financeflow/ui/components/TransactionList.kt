package com.financeflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.financeflow.transactions.TransactionEntity
import com.financeflow.transactions.TransactionType
import com.financeflow.R
import java.text.NumberFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TransactionList(
    transactions: List<TransactionEntity>,
    onEdit: (TransactionEntity) -> Unit,
    onDelete: (TransactionEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM")
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 280.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(transactions, key = { it.id }) { transaction ->
            TransactionRow(
                transaction = transaction,
                dateFormatter = dateFormatter,
                currencyFormatter = currencyFormatter,
                onEdit = onEdit,
                onDelete = onDelete
            )
        }
    }
}

@Composable
private fun TransactionRow(
    transaction: TransactionEntity,
    dateFormatter: DateTimeFormatter,
    currencyFormatter: NumberFormat,
    onEdit: (TransactionEntity) -> Unit,
    onDelete: (TransactionEntity) -> Unit
) {
    val amountColor = if (transaction.type == TransactionType.INCOME) Color(0xFF5EFCE8) else Color(0xFFFF6CAB)
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = Color(0x331E1A5B))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .heightIn(min = 54.dp)
                    .width(6.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            listOf(amountColor, amountColor.copy(alpha = 0.2f))
                        ),
                        shape = MaterialTheme.shapes.extraLarge
                    )
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${transaction.category} • ${dateFormatter.format(transaction.occurredAt.atZone(ZoneId.systemDefault()).toLocalDate())}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(0.7f)) {
                Text(
                    text = currencyFormatter.format(transaction.amount * if (transaction.type == TransactionType.EXPENSE) -1 else 1),
                    color = amountColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(
                        id = if (transaction.type == TransactionType.INCOME) R.string.income_label else R.string.expense_label
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
            FilledIconButton(
                onClick = { onEdit(transaction) },
                colors = FilledIconButtonDefaults.filledIconButtonColors(containerColor = Color.White.copy(alpha = 0.12f))
            ) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
            }
            FilledIconButton(
                onClick = { onDelete(transaction) },
                colors = FilledIconButtonDefaults.filledIconButtonColors(containerColor = Color.White.copy(alpha = 0.12f))
            ) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
            }
        }
    }
}
