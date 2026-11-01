package com.pmdm.fichajuego.navegacion

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import com.pmdm.fichajuego.R
import kotlinx.serialization.Serializable

// Cada pantalla es una clave: NavKey + @Serializable para que la pila se pueda guardar.

// Sin argumentos: data object
@Serializable
data object Biblioteca : NavKey

@Serializable
data object Estadisticas : NavKey

// Con argumentos: data class. Pasamos solo el id, no el Juego entero
@Serializable
data class DetalleJuego(val juegoId: Int) : NavKey

/** Pestañas de la barra inferior: cada una tiene su pantalla raíz y su propia pila. */
enum class Pestana(val raiz: NavKey, @StringRes val etiqueta: Int, @DrawableRes val icono: Int) {
    Inicio(Biblioteca, R.string.pestana_biblioteca, R.drawable.ic_biblioteca),
    Resumen(Estadisticas, R.string.pestana_estadisticas, R.drawable.ic_estadisticas)
}
