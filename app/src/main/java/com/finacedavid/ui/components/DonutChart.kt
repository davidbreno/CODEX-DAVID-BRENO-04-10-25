package com.finacedavid.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.dp
import com.finacedavid.domain.model.DonutSlice
import kotlin.math.min

@Composable
fun DonutChart(
    modifier: Modifier = Modifier,
    slices: List<DonutSlice>,
    strokeWidth: Float = 36f
) {
    Canvas(modifier = modifier) {
        val total = slices.fold(0f) { acc, slice -> acc + slice.value.toFloat() }
        if (total <= 0f) {
            drawArc(
                color = Color.LightGray,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(width = strokeWidth)
            )
            return@Canvas
        }
        var startAngle = -90f
        val diameter = min(size.width, size.height)
        val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
        val rect = Size(diameter, diameter)
        slices.forEach { slice ->
            val sweep = (slice.value.toFloat() / total) * 360f
            drawArc(
                color = Color(slice.color),
                startAngle = startAngle,
                sweepAngle = sweep,
                useCenter = false,
                style = Stroke(width = strokeWidth),
                topLeft = topLeft,
                size = rect
            )
            startAngle += sweep
        }
    }
}
