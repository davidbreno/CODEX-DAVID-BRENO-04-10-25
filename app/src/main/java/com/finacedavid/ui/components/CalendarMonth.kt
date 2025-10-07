package com.finacedavid.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.finacedavid.domain.model.CalendarDaySummary
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CalendarMonth(
    modifier: Modifier = Modifier,
    currentMonth: LocalDate,
    summaries: List<CalendarDaySummary>,
    onDaySelected: (LocalDate) -> Unit
) {
    val locale = Locale("pt", "BR")
    Column(modifier = modifier) {
        Text(
            text = currentMonth.month.getDisplayName(TextStyle.FULL, locale).replaceFirstChar { it.uppercase(locale) } +
                " ${currentMonth.year}",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        val firstDayOfMonth = currentMonth.withDayOfMonth(1)
        val firstDayOfWeek = firstDayOfMonth.dayOfWeek.value % 7
        val daysInMonth = currentMonth.lengthOfMonth()
        val items = (0 until firstDayOfWeek).map { null } +
            (1..daysInMonth).map { day ->
                val date = currentMonth.withDayOfMonth(day)
                summaries.find { it.date == date }
            }
        val weekDays = listOf("D", "S", "T", "Q", "Q", "S", "S")
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            weekDays.forEach { day ->
                Text(
                    text = day,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyVerticalGrid(columns = GridCells.Fixed(7), userScrollEnabled = false) {
            items(items.size) { index ->
                val summary = items[index]
                if (summary == null) {
                    Box(modifier = Modifier.size(48.dp))
                } else {
                    val amountText = if (summary.total.signum() == 0) "" else summary.total.toPlainString()
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .padding(4.dp)
                            .clickable { onDaySelected(summary.date) }
                            .background(
                                if (summary.hasEntries) MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)
                                else MaterialTheme.colorScheme.surface,
                                shape = MaterialTheme.shapes.medium
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = summary.date.dayOfMonth.toString(), fontWeight = FontWeight.Bold)
                            if (amountText.isNotEmpty()) {
                                Text(
                                    text = amountText,
                                    style = MaterialTheme.typography.labelLarge,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
