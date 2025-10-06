package com.financeflow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF006782),
    onPrimary = Color.White,
    secondary = Color(0xFF4F6268),
    tertiary = Color(0xFF695779)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF50C8FF),
    onPrimary = Color(0xFF00344B),
    secondary = Color(0xFFBAC8CC),
    tertiary = Color(0xFFD3C1E2)
)

@Immutable
data class GradientTheme(val gradient: Brush, val isDark: Boolean, val name: String)

val LocalGradientTheme = staticCompositionLocalOf {
    GradientTheme(
        gradient = FinanceFlowPalettes.Nebula.gradient,
        isDark = FinanceFlowPalettes.Nebula.isDark,
        name = FinanceFlowPalettes.Nebula.name
    )
}

@Composable
fun FinanceFlowTheme(
    gradientTheme: FinanceFlowGradient = FinanceFlowPalettes.Nebula,
    useDarkTheme: Boolean = gradientTheme.isDark || isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) DarkColors else LightColors

    CompositionLocalProvider(
        LocalGradientTheme provides GradientTheme(
            gradient = gradientTheme.gradient,
            isDark = gradientTheme.isDark,
            name = gradientTheme.name
        )
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
