package com.pmdm.fichajuego.model

import androidx.annotation.DrawableRes
import com.pmdm.fichajuego.R

/**
 * Datos que muestra una ficha. Es una clase de Kotlin normal: no sabe nada de Compose.
 * La portada es el identificador de un recurso de res/drawable (R.drawable.…).
 * El id es único y no cambia nunca: lo usamos como key en las listas.
 */
data class Juego(
    val id: Int,
    val titulo: String,
    val genero: String,
    val anio: Int,
    val nota: Double,
    @DrawableRes val portada: Int,
    val esNovedad: Boolean = false
)

// Catálogo de ejemplo en memoria (en la UT4 vendrá de una base de datos o de internet)
val catalogo = listOf(
    Juego(1, "Hollow Knight", "Metroidvania", 2017, 4.8, R.drawable.img_portada_hollow, esNovedad = true),
    Juego(2, "Stardew Valley", "Simulación", 2016, 4.7, R.drawable.img_portada_stardew),
    Juego(3, "Celeste", "Plataformas", 2018, 4.6, R.drawable.img_portada_celeste),
    Juego(4, "Hades", "Roguelike", 2020, 4.9, R.drawable.img_portada_hades, esNovedad = true),
    Juego(5, "Outer Wilds", "Exploración", 2019, 4.5, R.drawable.img_portada_outerwilds),
    Juego(6, "Baba Is You", "Puzles", 2019, 4.4, R.drawable.img_portada_bloques)
)

// Atajos para las previews
val juegoEjemplo = catalogo[0]
val otroJuego = catalogo[1]
