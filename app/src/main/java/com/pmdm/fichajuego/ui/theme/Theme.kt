package com.pmdm.fichajuego.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Indigo40,
    onPrimary = Color.White,
    primaryContainer = IndigoContainer90,
    onPrimaryContainer = OnIndigoContainer10,
    secondary = Teal40,
    onSecondary = Color.White,
    secondaryContainer = TealContainer90,
    onSecondaryContainer = OnTealContainer10,
    tertiary = Ambar40,
    onTertiary = Color.White,
    tertiaryContainer = AmbarContainer90,
    onTertiaryContainer = OnAmbarContainer10,
    background = Fondo99,
    onBackground = Texto10,
    surface = Fondo99,
    onSurface = Texto10,
    surfaceVariant = Variante90,
    onSurfaceVariant = OnVariante30
)

private val DarkColorScheme = darkColorScheme(
    primary = Indigo80,
    onPrimary = OnIndigo20,
    primaryContainer = IndigoContainer30,
    onPrimaryContainer = IndigoContainer90,
    secondary = Teal80,
    onSecondary = OnTeal20,
    secondaryContainer = TealContainer30,
    onSecondaryContainer = TealContainer90,
    tertiary = Ambar80,
    onTertiary = OnAmbar20,
    tertiaryContainer = AmbarContainer30,
    onTertiaryContainer = AmbarContainer90,
    background = Fondo6,
    onBackground = Texto90,
    surface = Fondo6,
    onSurface = Texto90,
    surfaceVariant = Variante30,
    onSurfaceVariant = OnVariante80
)

@Composable
fun FichaJuegoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Desactivado: queremos que la app se vea siempre con los colores de nuestra marca.
    // Con true, en Android 12+ los colores salen del fondo de pantalla del usuario.
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
