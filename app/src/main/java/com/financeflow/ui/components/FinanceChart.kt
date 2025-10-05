package com.financeflow.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.financeflow.analytics.ChartEntry
import java.time.format.DateTimeFormatter

@Composable
fun FinanceChart(entries: List<ChartEntry>, modifier: Modifier = Modifier) {
    if (entries.isEmpty()) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
        ) {
            Text(
                text = "No data yet",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
        }
        return
    }

    val formatter = DateTimeFormatter.ofPattern("MM/dd")

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(200.dp)) {
            val incomePath = Path()
            val expensePath = Path()
            val stepX = size.width / (entries.size - 1).coerceAtLeast(1)
            val maxValue = entries.flatMap { listOf(it.income, it.expense) }.maxOrNull() ?: 0.0
            val minValue = 0.0
            val range = (maxValue - minValue).coerceAtLeast(1.0)
            val textPaint = android.graphics.Paint().apply {
                color = android.graphics.Color.WHITE
                textSize = 32f
                textAlign = android.graphics.Paint.Align.CENTER
            }

            entries.forEachIndexed { index, entry ->
                val x = stepX * index
                val incomeY = size.height - ((entry.income - minValue) / range * size.height).toFloat()
                val expenseY = size.height - ((entry.expense - minValue) / range * size.height).toFloat()
                if (index == 0) {
                    incomePath.moveTo(x, incomeY)
                    expensePath.moveTo(x, expenseY)
                } else {
                    incomePath.lineTo(x, incomeY)
                    expensePath.lineTo(x, expenseY)
                }
            }

            drawPath(
                path = incomePath,
                color = Color(0xFF4CAF50),
                style = Stroke(width = 6f, cap = StrokeCap.Round)
            )
            drawPath(
                path = expensePath,
                color = Color(0xFFF44336),
                style = Stroke(width = 6f, cap = StrokeCap.Round)
            )

            entries.forEachIndexed { index, entry ->
                val x = stepX * index
                drawContext.canvas.nativeCanvas.drawText(
                    formatter.format(entry.date),
                    x,
                    size.height + 24f,
                    textPaint
                )
            }
        }
    }
}
