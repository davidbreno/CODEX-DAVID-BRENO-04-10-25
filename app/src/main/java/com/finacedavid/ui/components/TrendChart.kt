package com.finacedavid.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.unit.dp
import com.finacedavid.domain.model.TrendPoint
import kotlin.math.max
import kotlin.math.min

@Composable
fun TrendChart(
    modifier: Modifier = Modifier,
    points: List<TrendPoint>,
    lineColor: Color = Color(0xFF1ABCFE)
) {
    Canvas(modifier = modifier) {
        if (points.isEmpty()) return@Canvas
        val maxValue = points.maxOf { it.balance.toFloat() }
        val minValue = points.minOf { it.balance.toFloat() }
        val range = max(1f, maxValue - minValue)

        val stepX = size.width / max(1, points.size - 1)
        val path = Path()
        points.forEachIndexed { index, point ->
            val x = stepX * index
            val normalized = (point.balance.toFloat() - minValue) / range
            val y = size.height - (normalized * size.height)
            if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        drawPath(path, color = lineColor, style = Stroke(width = 6f))
    }
}
