package com.smsforwarder.app.ui.theme

import androidx.compose.foundation.isSystemInDarkMode
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFFC4B3BA),
    onSecondary = Color(0xFF000000),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFFFFFFF),
    error = Color(0xFFCF6679),
    onError = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF2D3748),
    onSurfaceVariant = Color(0xFFD1D5DB)
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6750A4),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFFC4B3BA),
    onSecondary = Color(0xFF000000),
    surface = Color(0xFFFEFEFE),
    onSurface = Color(0xFF1C1B1F),
    error = Color(0xFFCF6679),
    onError = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFFE7E0EC),
    onSurfaceVariant = Color(0xFF49454F)
)

@Composable
fun SmsForwarderTheme(content: @Composable () -> Unit) {
    val colorScheme = if (isSystemInDarkMode()) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}