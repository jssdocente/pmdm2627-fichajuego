package com.pmdm.fichajuego.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

// Radios de las esquinas: los componentes de Material los toman de aquí
// (las Card usan medium) y nosotros los leemos con MaterialTheme.shapes.
val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp)
)
