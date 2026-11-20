package com.mithila.smartrefresh.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val MidnightCharcoal = Color(0xFF101820)
val SurfaceDark = Color(0xFF182430)
val CardDark = Color(0xFF1E2D3D)
val ChampagneGold = Color(0xFFD7B56D)
val ElectricTeal = Color(0xFF00D2C6)
val TextPrimary = Color(0xFFF5F7FA)
val TextSecondary = Color(0xFFA0B0C0)
val DangerRed = Color(0xFFFF5C5C)
val SuccessGreen = Color(0xFF2EE59D)

private val DarkColorScheme = darkColorScheme(
    primary = ChampagneGold,
    secondary = ElectricTeal,
    background = MidnightCharcoal,
    surface = SurfaceDark,
    onPrimary = MidnightCharcoal,
    onSecondary = MidnightCharcoal,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

@Composable
fun MithilaSmartTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        content = content
    )
}
