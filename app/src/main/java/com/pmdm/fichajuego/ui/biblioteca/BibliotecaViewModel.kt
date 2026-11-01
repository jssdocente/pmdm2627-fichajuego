package com.pmdm.fichajuego.ui.biblioteca

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmdm.fichajuego.data.JuegoRepositorio
import com.pmdm.fichajuego.model.Juego
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class BibliotecaUiState(
    val juegos: List<Juego> = emptyList(),
    val partidas: Map<Int, Int> = emptyMap()
)

/**
 * ViewModel de la biblioteca. No guarda los datos: los recibe del repositorio
 * y los transforma en el estado que necesita su pantalla.
 */
class BibliotecaViewModel(
    private val repositorio: JuegoRepositorio
) : ViewModel() {

    // El estado se DERIVA del repositorio: cada vez que cambian los juegos
    // o las partidas, combine vuelve a calcular el estado de la pantalla
    val uiState: StateFlow<BibliotecaUiState> =
        combine(repositorio.juegos, repositorio.partidas) { juegos, partidas ->
            BibliotecaUiState(juegos = juegos, partidas = partidas)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = BibliotecaUiState(repositorio.juegos.value, repositorio.partidas.value)
        )

    // Los eventos ya no tocan el estado: piden el cambio al repositorio,
    // y el estado se actualiza solo porque lo está observando
    fun onSumarPartida(juegoId: Int) = repositorio.sumarPartida(juegoId)

    fun onReiniciarPartidas() = repositorio.reiniciarPartidas()

    fun onAnadirJuego(titulo: String, genero: String, anio: Int, nota: Double) =
        repositorio.anadirJuego(titulo, genero, anio, nota)
}
