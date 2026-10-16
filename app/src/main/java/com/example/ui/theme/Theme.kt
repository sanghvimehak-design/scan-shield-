package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ScanShieldLightColorScheme = lightColorScheme(
    primary = DeepPurple,
    onPrimary = Color.White,
    primaryContainer = LavenderSubtle,
    onPrimaryContainer = DeepPurpleDark,
    secondary = LavenderPrimary,
    onSecondary = DarkInk,
    secondaryContainer = MintSubtle,
    onSecondaryContainer = MintDarkText,
    tertiary = ButterYellow,
    onTertiary = ButterYellowDarkText,
    background = WarmCream,
    onBackground = DarkInk,
    surface = CardSurfaceLight,
    onSurface = DarkInk,
    surfaceVariant = LavenderSubtle,
    onSurfaceVariant = DarkInkLight,
    error = WarningRose,
    onError = Color.White,
    errorContainer = WarningRoseLight,
    onErrorContainer = WarningRoseDark,
    outline = CardBorderLavender
)

private val ScanShieldDarkColorScheme = darkColorScheme(
    primary = LavenderPrimary,
    onPrimary = DarkInk,
    primaryContainer = DeepPurple,
    onPrimaryContainer = Color.White,
    secondary = MintAccent,
    onSecondary = DarkInk,
    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = MintAccent,
    tertiary = ButterYellow,
    onTertiary = DarkInk,
    background = DarkBackground,
    onBackground = DarkText,
    surface = DarkSurface,
    onSurface = DarkText,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextMuted,
    error = WarningRose,
    onError = Color.White,
    outline = DarkSurfaceVariant
)

private val ScanShieldHighContrastColorScheme = lightColorScheme(
    primary = Color(0xFF321E78),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEADBFF),
    onPrimaryContainer = Color.Black,
    secondary = Color(0xFF005234),
    onSecondary = Color.White,
    tertiary = Color(0xFF6B4E00),
    onTertiary = Color.White,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    surfaceVariant = Color(0xFFF1EFFF),
    onSurfaceVariant = Color.Black,
    error = Color(0xFFB00020),
    onError = Color.White,
    outline = Color.Black
)

@Composable
fun ScanShieldTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isHighContrast: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        isHighContrast -> ScanShieldHighContrastColorScheme
        darkTheme -> ScanShieldDarkColorScheme
        else -> ScanShieldLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
