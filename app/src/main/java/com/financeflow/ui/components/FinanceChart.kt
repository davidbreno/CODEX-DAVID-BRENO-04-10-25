package com.financeflow.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.stringResource
import com.financeflow.analytics.ChartEntry
import com.financeflow.R
import java.time.format.DateTimeFormatter
import kotlin.math.max
import kotlin.math.min

@Composable
fun FinanceChart(entries: List<ChartEntry>, modifier: Modifier = Modifier) {
    if (entries.isEmpty()) {
        Card(
            modifier = modifier,
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f))
        ) {
            Text(
                text = stringResource(id = R.string.chart_empty),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(horizontal = 24.dp),
                textAlign = TextAlign.Center
            )
        }
        return
    }

    val formatter = DateTimeFormatter.ofPattern("MMM")
    val netValues = entries.map { it.income - it.expense }
    val maxValue = max(netValues.maxOrNull() ?: 0.0, 0.0)
    val minValue = min(netValues.minOrNull() ?: 0.0, 0.0)
    val range = (maxValue - minValue).takeIf { it != 0.0 } ?: 1.0

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.3f))
    ) {
        Canvas(modifier = Modifier.fillMaxWidth().height(220.dp)) {
            val path = Path()
            val fillPath = Path()
            val highlightColor = Color(0xFF6E8BFF)
            val gradient = Brush.verticalGradient(
                colors = listOf(highlightColor.copy(alpha = 0.4f), Color.Transparent)
            )
            val textPaint = Paint().apply {
                color = android.graphics.Color.WHITE
                textAlign = Paint.Align.CENTER
                textSize = 32f
            }

            val stepX = if (entries.size == 1) size.width else size.width / (entries.size - 1)

            entries.forEachIndexed { index, entry ->
                val value = entry.income - entry.expense
                val normalized = ((value - minValue) / range).toFloat()
                val x = stepX * index
                val y = size.height - (normalized * size.height)
                if (index == 0) {
                    path.moveTo(x, y)
                    fillPath.moveTo(x, size.height)
                    fillPath.lineTo(x, y)
                } else {
                    path.lineTo(x, y)
                    fillPath.lineTo(x, y)
                }
            }

            fillPath.lineTo(stepX * (entries.size - 1), size.height)
            fillPath.close()

            drawPath(
                path = fillPath,
                brush = gradient
            )

            drawPath(
                path = path,
                color = highlightColor,
                style = Stroke(width = 10f, cap = StrokeCap.Round)
            )

            entries.forEachIndexed { index, entry ->
                val value = entry.income - entry.expense
                val normalized = ((value - minValue) / range).toFloat()
                val x = stepX * index
                val y = size.height - (normalized * size.height)
                drawCircle(color = Color.White, radius = 10f, center = Offset(x, y))
            }

            entries.forEachIndexed { index, entry ->
                val x = stepX * index
                drawContext.canvas.nativeCanvas.drawText(
                    formatter.format(entry.date),
                    x,
                    size.height + 36f,
                    textPaint
                )
            }
        }
    }
}
