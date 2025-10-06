package com.financeflow.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.financeflow.R
import com.financeflow.analytics.AnalyticsSummary
import com.financeflow.authentication.UserEntity
import com.financeflow.transactions.BillEntity
import com.financeflow.transactions.DashboardSummary
import com.financeflow.transactions.TransactionEntity
import com.financeflow.ui.components.BillList
import com.financeflow.ui.components.FinanceChart
import com.financeflow.ui.components.TransactionList
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

@Composable
fun DashboardScreen(
    summary: DashboardSummary,
    analytics: AnalyticsSummary,
    user: UserEntity?,
    onAddTransaction: () -> Unit,
    onAddIncome: () -> Unit,
    onAddExpense: () -> Unit,
    onEditTransaction: (TransactionEntity) -> Unit,
    onDeleteTransaction: (TransactionEntity) -> Unit,
    onOpenTheme: () -> Unit,
    onToggleBill: (BillEntity, Boolean) -> Unit,
    onAddBill: () -> Unit,
    onEditBill: (BillEntity) -> Unit,
    onDeleteBill: (BillEntity) -> Unit
) {
    val currencyFormatter = remember { NumberFormat.getCurrencyInstance(Locale("pt", "BR")) }
    val decimalFormat = remember { DecimalFormat("#0.0") }
    val balanceText = currencyFormatter.format(analytics.balance)
    val incomeText = currencyFormatter.format(summary.incomeTotal)
    val expenseText = currencyFormatter.format(summary.expenseTotal)
    val billsTotal = summary.bills.sumOf { it.amount }
    val billsText = currencyFormatter.format(billsTotal)
    val totalFlow = (summary.incomeTotal + summary.expenseTotal).takeIf { it > 0 } ?: 1.0
    val changePercent = if (totalFlow == 0.0) 0.0 else analytics.balance / totalFlow
    val incomeShare = (summary.incomeTotal / totalFlow).coerceIn(0.0, 1.0)
    val expenseShare = (summary.expenseTotal / totalFlow).coerceIn(0.0, 1.0)
    val billsShare = if (summary.incomeTotal <= 0.0) 0.0 else (billsTotal / summary.incomeTotal).coerceIn(0.0, 1.0)
    val userName = remember(user) {
        user?.email?.substringBefore('@')?.replaceFirstChar { char ->
            if (char.isLowerCase()) char.titlecase(Locale.getDefault()) else char.toString()
        } ?: "Thomas"
    }
    val changeText = decimalFormat.format(abs(changePercent) * 100)
    val changePrefix = if (changePercent >= 0) "+" else "-"

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.dashboard)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onOpenTheme) {
                        Icon(Icons.Default.Palette, contentDescription = stringResource(id = R.string.theme_settings))
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTransaction, containerColor = Color(0xFF7B5BFF)) {
                Icon(Icons.Default.Add, contentDescription = stringResource(id = R.string.add_transaction))
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OverviewCard(
                userName = userName,
                balance = balanceText,
                changeLabel = "$changePrefix$changeText%",
                isPositive = changePercent >= 0,
                onAddTransaction = onAddTransaction
            )
            PerformanceCard(
                balance = balanceText,
                income = incomeText,
                expense = expenseText,
                changePercent = "$changePrefix$changeText%",
                analytics = analytics
            )
            QuickActionsRow(
                onAddIncome = onAddIncome,
                onAddExpense = onAddExpense,
                onAddBill = onAddBill
            )
            AnalyticsRow(
                incomeShare = incomeShare,
                incomeLabel = incomeText,
                expenseShare = expenseShare,
                expenseLabel = expenseText,
                billsShare = billsShare,
                billsLabel = billsText
            )
            SectionCard(
                title = stringResource(id = R.string.bills_title),
                actionLabel = stringResource(id = R.string.add_bill),
                onAction = onAddBill
            ) {
                if (summary.bills.isEmpty()) {
                    EmptySectionPlaceholder(text = stringResource(id = R.string.bills_label))
                } else {
                    BillList(
                        bills = summary.bills,
                        onTogglePaid = onToggleBill,
                        onEdit = onEditBill,
                        onDelete = onDeleteBill,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            SectionCard(
                title = stringResource(id = R.string.transactions_title),
                actionLabel = stringResource(id = R.string.add_transaction),
                onAction = onAddTransaction
            ) {
                if (summary.transactions.isEmpty()) {
                    EmptySectionPlaceholder(text = stringResource(id = R.string.transactions_title))
                } else {
                    TransactionList(
                        transactions = summary.transactions,
                        onEdit = onEditTransaction,
                        onDelete = onDeleteTransaction,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun OverviewCard(
    userName: String,
    balance: String,
    changeLabel: String,
    isPositive: Boolean,
    onAddTransaction: () -> Unit
) {
    val displayName = if (userName.isBlank()) stringResource(id = R.string.app_name) else userName
    val avatarLetter = displayName.firstOrNull()?.uppercase() ?: "F"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0x331E1A5B)
        )
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Color(0xFF6E8BFF), Color(0xFFB583F0))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = avatarLetter, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
                Column(modifier = Modifier.padding(start = 16.dp)) {
                    Text(text = stringResource(id = R.string.welcome_back), style = MaterialTheme.typography.labelLarge)
                    Text(
                        text = displayName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = stringResource(id = R.string.balance_label), style = MaterialTheme.typography.bodyLarge)
            Text(
                text = balance,
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null)
                Text(
                    text = stringResource(id = R.string.last_30_days),
                    modifier = Modifier.padding(start = 8.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                ChangeBadge(label = changeLabel, isPositive = isPositive)
            }
            Spacer(modifier = Modifier.height(20.dp))
            FilledTonalButton(onClick = onAddTransaction, shape = RoundedCornerShape(20.dp)) {
                Icon(Icons.Default.Add, contentDescription = null)
                Text(text = stringResource(id = R.string.add_transaction), modifier = Modifier.padding(start = 8.dp))
            }
        }
    }
}

@Composable
private fun ChangeBadge(label: String, isPositive: Boolean) {
    val badgeColor = if (isPositive) Color(0xFF23CE6B) else Color(0xFFFF5376)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .background(badgeColor.copy(alpha = 0.2f))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = badgeColor, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun PerformanceCard(
    balance: String,
    income: String,
    expense: String,
    changePercent: String,
    analytics: AnalyticsSummary
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x331E1A5B))
    ) {
        Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(id = R.string.performance_label), style = MaterialTheme.typography.titleMedium)
                    Text(text = changePercent, color = Color(0xFF6E8BFF), fontWeight = FontWeight.SemiBold)
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = stringResource(id = R.string.balance_label))
                    Text(text = balance, fontWeight = FontWeight.Bold)
                }
            }
            FinanceChart(entries = analytics.chart, modifier = Modifier.fillMaxWidth())
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = stringResource(id = R.string.income_label), style = MaterialTheme.typography.labelLarge)
                    Text(text = income, fontWeight = FontWeight.Bold)
                }
                Box(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .height(48.dp)
                        .width(1.dp)
                        .background(Color.White.copy(alpha = 0.2f))
                )
                Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                    Text(text = stringResource(id = R.string.expense_label), style = MaterialTheme.typography.labelLarge)
                    Text(text = expense, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun QuickActionsRow(
    onAddIncome: () -> Unit,
    onAddExpense: () -> Unit,
    onAddBill: () -> Unit
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        QuickActionButton(
            title = stringResource(id = R.string.income_label),
            subtitle = stringResource(id = R.string.quick_action_income),
            icon = { Icon(Icons.Default.ArrowDownward, contentDescription = null) },
            background = Brush.linearGradient(listOf(Color(0xFF5EFCE8), Color(0xFF736EFE))),
            onClick = onAddIncome
        )
        QuickActionButton(
            title = stringResource(id = R.string.expense_label),
            subtitle = stringResource(id = R.string.quick_action_expense),
            icon = { Icon(Icons.Default.ArrowUpward, contentDescription = null) },
            background = Brush.linearGradient(listOf(Color(0xFFFF6CAB), Color(0xFF736EFE))),
            onClick = onAddExpense
        )
        QuickActionButton(
            title = stringResource(id = R.string.bills_label),
            subtitle = stringResource(id = R.string.quick_action_bill),
            icon = { Icon(Icons.Default.CalendarMonth, contentDescription = null) },
            background = Brush.linearGradient(listOf(Color(0xFFFF9A8B), Color(0xFFFF6A88))),
            onClick = onAddBill
        )
    }
}

@Composable
private fun QuickActionButton(
    title: String,
    subtitle: String,
    icon: @Composable () -> Unit,
    background: Brush,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(24.dp),
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Column(
            modifier = Modifier
                .background(background, shape = RoundedCornerShape(24.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Text(text = title, fontWeight = FontWeight.SemiBold)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun AnalyticsRow(
    incomeShare: Double,
    incomeLabel: String,
    expenseShare: Double,
    expenseLabel: String,
    billsShare: Double,
    billsLabel: String
) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        RadialMetricCard(
            title = stringResource(id = R.string.income_label),
            value = incomeLabel,
            percent = incomeShare,
            accent = Color(0xFF5EFCE8)
        )
        RadialMetricCard(
            title = stringResource(id = R.string.expense_label),
            value = expenseLabel,
            percent = expenseShare,
            accent = Color(0xFFFF6CAB)
        )
        RadialMetricCard(
            title = stringResource(id = R.string.bills_label),
            value = billsLabel,
            percent = billsShare,
            accent = Color(0xFFFFD369)
        )
    }
}

@Composable
private fun RadialMetricCard(title: String, value: String, percent: Double, accent: Color) {
    Card(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x331E1A5B))
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Canvas(modifier = Modifier.size(86.dp)) {
                val strokeWidth = 12.dp.toPx()
                drawArc(
                    color = accent.copy(alpha = 0.2f),
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                drawArc(
                    color = accent,
                    startAngle = -90f,
                    sweepAngle = (360 * percent).toFloat(),
                    useCenter = false,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
            }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = title, style = MaterialTheme.typography.labelLarge, textAlign = TextAlign.Center)
                Text(text = value, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    actionLabel: String? = null,
    onAction: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0x1A1E1A5B))
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                if (actionLabel != null && onAction != null) {
                    TextButton(onClick = onAction) {
                        Text(text = actionLabel)
                    }
                }
            }
            content()
        }
    }
}

@Composable
private fun EmptySectionPlaceholder(text: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(id = R.string.empty_section),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White.copy(alpha = 0.4f)
        )
    }
}
