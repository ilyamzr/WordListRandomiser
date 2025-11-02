package com.example.randomizerapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryGreen,
    onPrimary = TextDark,
    secondary = ButtonBackground,
    tertiary = ButtonBackground,
    background = DarkBackground,
    surface = DarkSurface,
    onBackground = TextLight,
    onSurface = TextLight,
    onSurfaceVariant = TextLight, // Используем для некоторых текстов
    error = Color.Red,
    onError = Color.White
)

@Composable
fun RandomizerAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Можно принудительно установить darkTheme = true
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme // Всегда используем темную тему, как на мокапах

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Определите свою типографику, если нужно
        content = content
    )
}