package com.agendachat

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Mint = Color(0xFFA8EBCB)
val OnMint = Color(0xFF0B3D2A)

private val LightColors = lightColorScheme(
    primary = Color(0xFF0F6B49),
    onPrimary = Color.White,
    primaryContainer = Mint,
    onPrimaryContainer = OnMint,
    secondaryContainer = Color(0xFFDDF3E8),
    onSecondaryContainer = OnMint,
    background = Color(0xFFF7F9FA),
    onBackground = Color(0xFF14201B),
    surface = Color(0xFFF7F9FA),
    onSurface = Color(0xFF14201B),
    surfaceVariant = Color(0xFFE8EDF0),
    onSurfaceVariant = Color(0xFF5B6B66),
    outline = Color(0xFFD3DBDF),
    error = Color(0xFFC0392B),
)

private val DarkColors = darkColorScheme(
    primary = Mint,
    onPrimary = OnMint,
    primaryContainer = Mint,
    onPrimaryContainer = OnMint,
    secondaryContainer = Color(0xFF1C3A2E),
    onSecondaryContainer = Mint,
    background = Color(0xFF0D1014),
    onBackground = Color(0xFFE6EBE9),
    surface = Color(0xFF0D1014),
    onSurface = Color(0xFFE6EBE9),
    surfaceVariant = Color(0xFF171C22),
    onSurfaceVariant = Color(0xFF93A09B),
    outline = Color(0xFF262D34),
    error = Color(0xFFFF8A80),
)

@Composable
fun AgendaTheme(dark: Boolean, content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (dark) DarkColors else LightColors, content = content)
}
