package com.pmdm.fichajuego.ui.detalle

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmdm.fichajuego.data.JuegoRepositorio
import com.pmdm.fichajuego.model.Juego
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetalleUiState(
    val juego: Juego? = null,
    val partidas: Int = 0,
    val cronometro: Cronometro = Cronometro()
)

/**
 * La sesión de juego de este detalle. Ya no hace falta saber de qué juego es:
 * cada detalle tiene su propio ViewModel y, por tanto, su propio cronómetro.
 */
data class Cronometro(
    val segundos: Int = 0,
    val enMarcha: Boolean = false,
    val guardando: Boolean = false // la espera al terminar: mientras dura, los botones se bloquean
)

/**
 * ViewModel del detalle de UN juego: recibe su id en el constructor.
 * Cada entrada DetalleJuego de la pila tiene su propia instancia,
 * que se destruye cuando esa entrada sale de la pila.
 */
class DetalleViewModel(
    private val juegoId: Int,
    private val repositorio: JuegoRepositorio
) : ViewModel() {

    // El cronómetro es de esta pantalla, no un dato compartido: no va en el repositorio
    private val _cronometro = MutableStateFlow(Cronometro())

    val uiState: StateFlow<DetalleUiState> =
        combine(repositorio.juegos, repositorio.partidas, _cronometro) { juegos, partidas, cronometro ->
            DetalleUiState(
                juego = juegos.find { it.id == juegoId },
                partidas = partidas[juegoId] ?: 0,
                cronometro = cronometro
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DetalleUiState(
                juego = repositorio.juegos.value.find { it.id == juegoId },
                partidas = repositorio.partidas.value[juegoId] ?: 0
            )
        )

    // Ya sabe de qué juego es: el evento no necesita el id
    fun onSumarPartida() = repositorio.sumarPartida(juegoId)

    // Cronómetro: el mismo de la sesión 3.06, ahora en el ViewModel de su pantalla.
    // viewModelScope se cancela al destruir este ViewModel, es decir, al salir del detalle

    private var jobCronometro: Job? = null

    fun onEmpezar() {
        val cronometro = _cronometro.value
        if (cronometro.guardando || cronometro.enMarcha) return

        _cronometro.update { it.copy(enMarcha = true) }
        arrancarCronometro()
    }

    fun onPausar() {
        jobCronometro?.cancel()
        _cronometro.update { it.copy(enMarcha = false) }
    }

    fun onTerminar() {
        if (_cronometro.value.guardando) return

        jobCronometro?.cancel()
        _cronometro.update { it.copy(enMarcha = false, guardando = true) }
        viewModelScope.launch {
            delay(ESPERA_AL_TERMINAR_MS)
            repositorio.sumarPartida(juegoId)
            _cronometro.value = Cronometro()
        }
    }

    fun onSalirDeLaApp() {
        jobCronometro?.cancel()
    }

    fun onVolverALaApp() {
        if (_cronometro.value.enMarcha) arrancarCronometro()
    }

    private fun arrancarCronometro() {
        jobCronometro?.cancel() // nunca dos corrutinas contando a la vez
        jobCronometro = viewModelScope.launch {
            while (true) {
                delay(1_000)
                _cronometro.update { it.copy(segundos = it.segundos + 1) }
            }
        }
    }

    private companion object {
        const val ESPERA_AL_TERMINAR_MS = 2_000L
    }
}
