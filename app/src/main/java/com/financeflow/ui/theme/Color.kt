package com.financeflow.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

data class FinanceFlowGradient(
    val name: String,
    val gradient: Brush,
    val isDark: Boolean
)

object FinanceFlowPalettes {
    val Nebula = FinanceFlowGradient(
        name = "Nebula",
        gradient = Brush.verticalGradient(
            colors = listOf(Color(0xFF130F40), Color(0xFF1B1464), Color(0xFF0F3460))
        ),
        isDark = true
    )
    val Sunrise = FinanceFlowGradient(
        name = "Sunrise",
        gradient = Brush.verticalGradient(
            colors = listOf(Color(0xFFFF6F61), Color(0xFFFFA177))
        ),
        isDark = false
    )
    val Ocean = FinanceFlowGradient(
        name = "Ocean",
        gradient = Brush.verticalGradient(
            colors = listOf(Color(0xFF2193B0), Color(0xFF6DD5ED))
        ),
        isDark = false
    )
    val Forest = FinanceFlowGradient(
        name = "Forest",
        gradient = Brush.verticalGradient(
            colors = listOf(Color(0xFF11998E), Color(0xFF38EF7D))
        ),
        isDark = false
    )
    val Twilight = FinanceFlowGradient(
        name = "Twilight",
        gradient = Brush.verticalGradient(
            colors = listOf(Color(0xFF654EA3), Color(0xFFEE82DA))
        ),
        isDark = true
    )
    val Midnight = FinanceFlowGradient(
        name = "Midnight",
        gradient = Brush.verticalGradient(
            colors = listOf(Color(0xFF141E30), Color(0xFF243B55))
        ),
        isDark = true
    )

    val all = listOf(Nebula, Sunrise, Ocean, Forest, Twilight, Midnight)
}
