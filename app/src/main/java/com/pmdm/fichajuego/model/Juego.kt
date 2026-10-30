package com.pmdm.fichajuego.model

/**
 * Datos que muestra una ficha. Es una clase de Kotlin normal: no sabe nada de Compose.
 */
data class Juego(
    val titulo: String,
    val genero: String,
    val anio: Int,
    val nota: Double,
    val esNovedad: Boolean = false
)

// Datos de ejemplo para la app y las previews
val juegoEjemplo = Juego(
    titulo = "Hollow Knight",
    genero = "Metroidvania",
    anio = 2017,
    nota = 4.8,
    esNovedad = true
)

val otroJuego = Juego(
    titulo = "Stardew Valley",
    genero = "Simulación",
    anio = 2016,
    nota = 4.7
)
