package com.nurthure.monitor.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = AccentTeal,
    secondary = AccentPurple,
    tertiary = AccentGreen,
    background = Background,
    surface = CardBackground,
    onPrimary = CardBackground,
    onSecondary = CardBackground,
    onTertiary = CardBackground,
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = AlertCritical
)

@Composable
fun NurthureTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
