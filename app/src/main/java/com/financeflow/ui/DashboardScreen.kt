package com.financeflow.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.financeflow.analytics.AnalyticsSummary
import com.financeflow.transactions.BillEntity
import com.financeflow.transactions.DashboardSummary
import com.financeflow.transactions.TransactionEntity
import com.financeflow.ui.components.BillList
import com.financeflow.ui.components.FinanceChart
import com.financeflow.ui.components.TransactionList

@Composable
fun DashboardScreen(
    summary: DashboardSummary,
    analytics: AnalyticsSummary,
    onAddTransaction: () -> Unit,
    onEditTransaction: (TransactionEntity) -> Unit,
    onDeleteTransaction: (TransactionEntity) -> Unit,
    onOpenTheme: () -> Unit,
    onToggleBill: (BillEntity, Boolean) -> Unit,
    onAddBill: () -> Unit,
    onEditBill: (BillEntity) -> Unit,
    onDeleteBill: (BillEntity) -> Unit
) {
    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTransaction) {
                Icon(Icons.Default.Add, contentDescription = "Add Transaction")
            }
        },
        topBar = {
            TopAppBar(
                title = { Text("Dashboard") },
                actions = {
                    IconButton(onClick = onOpenTheme) {
                        Icon(Icons.Default.Palette, contentDescription = "Theme")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SummaryCard(title = "Balance", value = analytics.balance)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                SummaryCard(
                    title = "Income",
                    value = analytics.income,
                    modifier = Modifier.weight(1f),
                    color = Color(0xFF4CAF50)
                )
                SummaryCard(
                    title = "Expenses",
                    value = analytics.expense,
                    modifier = Modifier.weight(1f),
                    color = Color(0xFFF44336)
                )
            }
            FinanceChart(entries = analytics.chart, modifier = Modifier.fillMaxWidth())
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Upcoming Bills", style = MaterialTheme.typography.titleLarge)
                IconButton(onClick = onAddBill) {
                    Icon(Icons.Default.Add, contentDescription = "Add Bill")
                }
            }
            BillList(
                bills = summary.bills,
                onTogglePaid = onToggleBill,
                onEdit = onEditBill,
                onDelete = onDeleteBill,
                modifier = Modifier.fillMaxWidth()
            )
            Text(text = "Recent Transactions", style = MaterialTheme.typography.titleLarge)
            TransactionList(
                transactions = summary.transactions,
                onEdit = onEditTransaction,
                onDelete = onDeleteTransaction,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SummaryCard(title: String, value: Double, modifier: Modifier = Modifier, color: Color = MaterialTheme.colorScheme.primary) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "$${"%.2f".format(value)}", style = MaterialTheme.typography.headlineSmall, color = color)
        }
    }
}
