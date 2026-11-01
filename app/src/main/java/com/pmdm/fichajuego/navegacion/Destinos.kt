package com.pmdm.fichajuego.navegacion

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

// Cada pantalla es una clave: NavKey + @Serializable para que la pila se pueda guardar.

// Sin argumentos: data object
@Serializable
data object Biblioteca : NavKey

// Con argumentos: data class. Pasamos solo el id, no el Juego entero
@Serializable
data class DetalleJuego(val juegoId: Int) : NavKey
