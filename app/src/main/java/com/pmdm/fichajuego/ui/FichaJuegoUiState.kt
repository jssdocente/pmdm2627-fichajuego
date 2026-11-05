package com.pmdm.fichajuego.ui

import com.pmdm.fichajuego.model.Juego
import com.pmdm.fichajuego.model.catalogo

/**
 * Todo lo que necesitan las pantallas para dibujarse, en un único objeto inmutable.
 * Nadie lo modifica: el ViewModel crea una copia nueva con copy(...) cada vez que cambia algo.
 */
data class FichaJuegoUiState(
    val juegos: List<Juego> = catalogo,
    val partidas: Map<Int, Int> = emptyMap(),
    val cronometro: Cronometro = Cronometro()
) {
    // Propiedades calculadas: se derivan de los datos, así que nunca se desincronizan
    val totalPartidas: Int
        get() = partidas.values.sum()

    fun partidasDe(juegoId: Int): Int = partidas[juegoId] ?: 0

    fun juego(juegoId: Int): Juego? = juegos.find { it.id == juegoId }
}

/**
 * La sesión de juego en curso. Todo lo que muestra el cronómetro sale de aquí:
 * el Job que cuenta el tiempo vive en el ViewModel, pero no guarda ningún dato de pantalla.
 */
data class Cronometro(
    val juegoId: Int? = null,      // a qué juego se está jugando; null si no hay sesión
    val segundos: Int = 0,
    val enMarcha: Boolean = false,
    val guardando: Boolean = false // la espera al terminar: mientras dura, los botones se bloquean
)
