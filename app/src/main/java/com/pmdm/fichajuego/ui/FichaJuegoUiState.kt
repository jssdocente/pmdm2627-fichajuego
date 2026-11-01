package com.pmdm.fichajuego.ui

import com.pmdm.fichajuego.model.Juego
import com.pmdm.fichajuego.model.catalogo

/**
 * Todo lo que necesitan las pantallas para dibujarse, en un único objeto inmutable.
 * Nadie lo modifica: el ViewModel crea una copia nueva con copy(...) cada vez que cambia algo.
 */
data class FichaJuegoUiState(
    val juegos: List<Juego> = catalogo,
    val partidas: Map<Int, Int> = emptyMap()
) {
    // Propiedades calculadas: se derivan de los datos, así que nunca se desincronizan
    val totalPartidas: Int
        get() = partidas.values.sum()

    fun partidasDe(juegoId: Int): Int = partidas[juegoId] ?: 0

    fun juego(juegoId: Int): Juego? = juegos.find { it.id == juegoId }
}
