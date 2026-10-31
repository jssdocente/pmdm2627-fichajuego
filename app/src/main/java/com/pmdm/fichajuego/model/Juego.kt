package com.pmdm.fichajuego.model

import androidx.annotation.DrawableRes
import com.pmdm.fichajuego.R

/**
 * Datos que muestra una ficha. Es una clase de Kotlin normal: no sabe nada de Compose.
 * La portada es el identificador de un recurso de res/drawable (R.drawable.…).
 */
data class Juego(
    val titulo: String,
    val genero: String,
    val anio: Int,
    val nota: Double,
    @DrawableRes val portada: Int,
    val esNovedad: Boolean = false
)

// Datos de ejemplo para la app y las previews
val juegoEjemplo = Juego(
    titulo = "Hollow Knight",
    genero = "Metroidvania",
    anio = 2017,
    nota = 4.8,
    portada = R.drawable.img_portada_hollow,
    esNovedad = true
)

val otroJuego = Juego(
    titulo = "Stardew Valley",
    genero = "Simulación",
    anio = 2016,
    nota = 4.7,
    portada = R.drawable.img_portada_stardew
)
