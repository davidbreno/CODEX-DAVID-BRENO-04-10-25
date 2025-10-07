package com.finacedavid.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.finacedavid.domain.model.DashboardSummary
import com.finacedavid.domain.model.PeriodFilter
import com.finacedavid.features.home.HomeUiState
import com.finacedavid.ui.components.CalendarMonth
import com.finacedavid.ui.components.DonutChart
import com.finacedavid.ui.components.TrendChart
import java.time.LocalDate

@Composable
fun HomeScreen(
    state: HomeUiState,
    onSelectFilter: (PeriodFilter) -> Unit,
    onNavigateToTransactions: () -> Unit,
    onNavigateToPayables: () -> Unit,
    onToggleTheme: () -> Unit,
    onShowCalendar: () -> Unit,
    formatCurrency: (java.math.BigDecimal) -> String
) {
    when (state) {
        HomeUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Carregando...")
            }
        }
        is HomeUiState.Loaded -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SummaryHeader(summary = state.summary, formatCurrency = formatCurrency)
                FilterChips(state.filter, onSelectFilter)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Distribuição", style = MaterialTheme.typography.titleMedium)
                        DonutChart(
                            modifier = Modifier
                                .size(220.dp)
                                .align(Alignment.CenterHorizontally),
                            slices = state.summary.donut
                        )
                    }
                }
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Evolução", style = MaterialTheme.typography.titleMedium)
                        TrendChart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp),
                            points = state.summary.trend
                        )
                    }
                }
                QuickActions(
                    onNavigateToTransactions = onNavigateToTransactions,
                    onNavigateToPayables = onNavigateToPayables,
                    onToggleTheme = onToggleTheme,
                    onShowCalendar = onShowCalendar
                )
            }
        }
    }
}

@Composable
private fun SummaryHeader(
    summary: DashboardSummary,
    formatCurrency: (java.math.BigDecimal) -> String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Saldo", style = MaterialTheme.typography.labelLarge)
            Text(
                formatCurrency(summary.balance),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text("Entradas", fontWeight = FontWeight.SemiBold)
                    Text(formatCurrency(summary.totalIncome), color = Color(0xFF22C55E))
                }
                Column {
                    Text("Saídas", fontWeight = FontWeight.SemiBold)
                    Text(formatCurrency(summary.totalOutcome), color = Color(0xFFEF4444))
                }
            }
        }
    }
}

@Composable
private fun FilterChips(
    selected: PeriodFilter,
    onSelect: (PeriodFilter) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        PeriodFilterChip("Hoje", selected is PeriodFilter.Today) { onSelect(PeriodFilter.Today) }
        PeriodFilterChip("7 dias", selected is PeriodFilter.Last7Days) { onSelect(PeriodFilter.Last7Days) }
        PeriodFilterChip("Mês", selected is PeriodFilter.CurrentMonth) { onSelect(PeriodFilter.CurrentMonth) }
    }
}

@Composable
private fun PeriodFilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier,
        shape = MaterialTheme.shapes.large,
        tonalElevation = if (selected) 6.dp else 0.dp,
        color = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface
    ) {
        Text(
            text = text,
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .clip(MaterialTheme.shapes.large)
                .clickableWithoutRipple(onClick),
            color = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun QuickActions(
    onNavigateToTransactions: () -> Unit,
    onNavigateToPayables: () -> Unit,
    onToggleTheme: () -> Unit,
    onShowCalendar: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        ActionButton("Entradas/Saídas", onNavigateToTransactions)
        ActionButton("Contas a Pagar", onNavigateToPayables)
        ActionButton("Calendário", onShowCalendar)
        ActionButton("Trocar Tema", onToggleTheme)
    }
}

@Composable
private fun ActionButton(text: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickableWithoutRipple(onClick),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Text(text, modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
    }
}
