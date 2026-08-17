package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MemphisColorScheme = lightColorScheme(
    primary = MemphisYellow,
    onPrimary = MemphisInk,
    primaryContainer = MemphisYellow,
    onPrimaryContainer = MemphisInk,
    secondary = MemphisPink,
    onSecondary = Color.White,
    secondaryContainer = MemphisPastelPink,
    onSecondaryContainer = MemphisInk,
    tertiary = MemphisBlue,
    onTertiary = Color.White,
    tertiaryContainer = MemphisPastelBlue,
    onTertiaryContainer = MemphisInk,
    background = MemphisCreamBg,
    onBackground = MemphisInk,
    surface = MemphisWhite,
    onSurface = MemphisInk,
    surfaceVariant = MemphisCream,
    onSurfaceVariant = MemphisInk,
    outline = MemphisBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MemphisColorScheme,
        typography = Typography,
        content = content
    )
}
