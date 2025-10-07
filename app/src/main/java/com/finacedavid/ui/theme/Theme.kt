package com.finacedavid.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = PrimaryBlue,
    secondary = AccentCyan,
    background = DarkSurface,
    surface = DarkCard,
    onBackground = Color(0xFFE2E8F0),
    onSurface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White
)

private val LightColors = lightColorScheme(
    primary = PrimaryBlue,
    secondary = AccentCyan,
    background = LightSurface,
    surface = LightCard,
    onBackground = Color(0xFF0F172A),
    onSurface = Color(0xFF0F172A),
    onPrimary = Color.White,
    onSecondary = Color.White
)

val LocalThemePreference = staticCompositionLocalOf { true }

@Composable
fun FinaceDavidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}

object FinaceDavidColors {
    val success: Color
        @Composable
        @ReadOnlyComposable
        get() = SuccessGreen
    val warning: Color
        @Composable
        @ReadOnlyComposable
        get() = WarningYellow
    val error: Color
        @Composable
        @ReadOnlyComposable
        get() = ErrorRed
}
