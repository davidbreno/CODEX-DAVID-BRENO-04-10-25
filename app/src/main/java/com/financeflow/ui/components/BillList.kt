package com.financeflow.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledIconButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.financeflow.transactions.BillEntity
import com.financeflow.R
import java.text.NumberFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun BillList(
    bills: List<BillEntity>,
    onTogglePaid: (BillEntity, Boolean) -> Unit,
    onEdit: (BillEntity) -> Unit,
    onDelete: (BillEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMM")
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("pt", "BR"))
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 240.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(bills, key = { it.id }) { bill ->
            BillRow(
                bill = bill,
                currencyFormatter = currencyFormatter,
                dateFormatter = dateFormatter,
                onTogglePaid = onTogglePaid,
                onEdit = onEdit,
                onDelete = onDelete
            )
        }
    }
}

@Composable
private fun BillRow(
    bill: BillEntity,
    currencyFormatter: NumberFormat,
    dateFormatter: DateTimeFormatter,
    onTogglePaid: (BillEntity, Boolean) -> Unit,
    onEdit: (BillEntity) -> Unit,
    onDelete: (BillEntity) -> Unit
) {
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
            Column(modifier = Modifier.weight(1f)) {
                Text(text = bill.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                Text(
                    text = stringResource(
                        id = R.string.bill_due_prefix,
                        dateFormatter.format(bill.dueDate.atZone(ZoneId.systemDefault()).toLocalDate())
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.6f)
                )
            }
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(0.7f)) {
                Text(text = currencyFormatter.format(bill.amount), fontWeight = FontWeight.Bold, color = Color(0xFFFFD369))
                Text(
                    text = stringResource(id = if (bill.isPaid) R.string.bill_paid else R.string.bill_pending),
                    color = Color.White.copy(alpha = 0.7f)
                )
            }
            Checkbox(
                checked = bill.isPaid,
                onCheckedChange = { onTogglePaid(bill, it) },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF5EFCE8),
                    uncheckedColor = Color.White.copy(alpha = 0.6f)
                )
            )
            FilledIconButton(
                onClick = { onEdit(bill) },
                colors = FilledIconButtonDefaults.filledIconButtonColors(containerColor = Color.White.copy(alpha = 0.12f))
            ) {
                Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
            }
            FilledIconButton(
                onClick = { onDelete(bill) },
                colors = FilledIconButtonDefaults.filledIconButtonColors(containerColor = Color.White.copy(alpha = 0.12f))
            ) {
                Icon(imageVector = Icons.Filled.Delete, contentDescription = null)
            }
        }
    }
}
