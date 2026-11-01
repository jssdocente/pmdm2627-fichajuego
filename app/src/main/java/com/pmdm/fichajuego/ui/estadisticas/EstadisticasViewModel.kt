package com.pmdm.fichajuego.ui.estadisticas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmdm.fichajuego.data.JuegoRepositorio
import com.pmdm.fichajuego.model.Juego
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/** Lo que muestra la pantalla de estadísticas, ya calculado. */
data class EstadisticasUiState(
    val totalPartidas: Int = 0,
    val numeroJuegos: Int = 0,
    val notaMedia: Double = 0.0,
    val masJugado: Juego? = null,
    val partidasMasJugado: Int = 0
)

/**
 * Los cálculos que antes hacía EstadisticasScreen viven ahora aquí:
 * la pantalla solo pinta, y los cálculos se pueden probar con un test.
 */
class EstadisticasViewModel(
    repositorio: JuegoRepositorio
) : ViewModel() {

    val uiState: StateFlow<EstadisticasUiState> =
        combine(repositorio.juegos, repositorio.partidas, ::calcularEstadisticas)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = calcularEstadisticas(repositorio.juegos.value, repositorio.partidas.value)
            )
}

private fun calcularEstadisticas(juegos: List<Juego>, partidas: Map<Int, Int>): EstadisticasUiState {
    // El más jugado: solo si hay alguna partida
    val masJugado = juegos
        .filter { (partidas[it.id] ?: 0) > 0 }
        .maxByOrNull { partidas[it.id] ?: 0 }
    return EstadisticasUiState(
        totalPartidas = partidas.values.sum(),
        numeroJuegos = juegos.size,
        notaMedia = juegos.map { it.nota }.average(),
        masJugado = masJugado,
        partidasMasJugado = masJugado?.let { partidas[it.id] } ?: 0
    )
}
