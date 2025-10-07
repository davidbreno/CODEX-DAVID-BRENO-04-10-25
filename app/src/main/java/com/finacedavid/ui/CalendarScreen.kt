package com.finacedavid.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.finacedavid.domain.model.CalendarDaySummary
import com.finacedavid.ui.components.CalendarMonth
import java.time.LocalDate

@Composable
fun CalendarScreen(
    currentMonth: LocalDate,
    summaries: List<CalendarDaySummary>,
    onDaySelected: (LocalDate) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Calendário", style = MaterialTheme.typography.titleMedium)
        CalendarMonth(
            currentMonth = currentMonth,
            summaries = summaries,
            onDaySelected = onDaySelected
        )
    }
}
