package com.smsforwarder.app.ui.theme

import androidx.compose.foundation.isSystemInDarkMode
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val DarkColorLight = lightColorScheme(
    colorScheme = ColorScheme(
        brightness = Brightness.Dark,
        primary = Color(0xFF6750A4),
        onPrimary = Color(0xFFFFFFFF),
        secondary = Color(0xFFC4B3BA),
        onSecondary = Color(0xFF000000),
        surface = Color(0xFF1E1E1E),
        onSurface = Color(0xFFFFFFFF),
        error = Color(0xFFCF6679),
        onError = Color(0xFFFFFFFF),
        surfaceVariant = Color(0xFF2D3748),
        onSurfaceVariant = Color(0xFFD1D5DB),
        variant = Color(0xFF4A5568),
        onVariant = Color(0xFFFFFFFF),
        grey = Color(0xFF718096),
    )
)

@Composable
fun SmsForwarderTheme(content: @Composable () -> Unit) {
    val m3 = rememberMaterial3Defaults(
        darkTheme = isSystemInDarkMode(),
        colorScheme = DarkColorLight
    )
    MaterialTheme3(m3) {
        content()
    }
}