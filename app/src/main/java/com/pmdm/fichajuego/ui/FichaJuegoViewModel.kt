package com.pmdm.fichajuego.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pmdm.fichajuego.R
import com.pmdm.fichajuego.model.Juego
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Guarda los juegos y las partidas, y es el único que puede cambiarlos.
 *
 * Sobrevive a los cambios de configuración (girar el móvil, modo oscuro, idioma):
 * la Activity se destruye y se vuelve a crear, pero recibe esta misma instancia.
 * No importa nada de Compose: por eso se puede probar con un test normal.
 */
class FichaJuegoViewModel : ViewModel() {

    // Estado mutable privado: solo se modifica desde dentro del ViewModel
    private val _uiState = MutableStateFlow(FichaJuegoUiState())

    // Estado público de solo lectura: la interfaz lo observa, pero no puede cambiarlo
    val uiState: StateFlow<FichaJuegoUiState> = _uiState.asStateFlow()

    // Eventos: la interfaz avisa de lo que ha hecho el usuario y el ViewModel decide qué cambia

    fun onSumarPartida(juegoId: Int) {
        _uiState.update { estado ->
            estado.copy(partidas = estado.partidas + (juegoId to estado.partidasDe(juegoId) + 1))
        }
    }

    fun onReiniciarPartidas() {
        _uiState.update { it.copy(partidas = emptyMap()) }
    }

    fun onAnadirJuego(titulo: String, genero: String, anio: Int, nota: Double) {
        _uiState.update { estado ->
            val nuevo = Juego(
                id = (estado.juegos.maxOfOrNull { it.id } ?: 0) + 1,
                titulo = titulo,
                genero = genero,
                anio = anio,
                nota = nota,
                portada = R.drawable.img_portada_generica,
                esNovedad = true
            )
            estado.copy(juegos = listOf(nuevo) + estado.juegos)
        }
    }

    // Cronómetro de la sesión de juego

    // El Job permite parar la corrutina que cuenta. No es estado de pantalla: no va en el UiState
    private var jobCronometro: Job? = null

    fun onEmpezar(juegoId: Int) {
        val cronometro = _uiState.value.cronometro
        // Reglas: ni durante la espera, ni si ya cuenta, ni si la sesión es de otro juego
        if (cronometro.guardando || cronometro.enMarcha) return
        if (cronometro.juegoId != null && cronometro.juegoId != juegoId) return

        actualizarCronometro { it.copy(juegoId = juegoId, enMarcha = true) }
        arrancarCronometro()
    }

    fun onPausar() {
        jobCronometro?.cancel()
        actualizarCronometro { it.copy(enMarcha = false) }
    }

    fun onTerminar() {
        val cronometro = _uiState.value.cronometro
        val juegoId = cronometro.juegoId ?: return
        if (cronometro.guardando) return

        jobCronometro?.cancel()
        // El bloqueo se activa ANTES de esperar y es parte del estado: la pantalla solo lo pinta
        actualizarCronometro { it.copy(enMarcha = false, guardando = true) }
        viewModelScope.launch {
            delay(ESPERA_AL_TERMINAR_MS)
            onSumarPartida(juegoId)
            actualizarCronometro { Cronometro() }
        }
    }

    // Al salir con el botón de inicio se para la corrutina, pero enMarcha sigue a true:
    // así, al volver, se sabe si hay que continuar o si el usuario lo había pausado
    fun onSalirDeLaApp() {
        jobCronometro?.cancel()
    }

    fun onVolverALaApp() {
        if (_uiState.value.cronometro.enMarcha) arrancarCronometro()
    }

    private fun arrancarCronometro() {
        jobCronometro?.cancel() // nunca dos corrutinas contando a la vez
        jobCronometro = viewModelScope.launch {
            while (true) {
                delay(1_000) // suspende sin bloquear el hilo principal
                actualizarCronometro { it.copy(segundos = it.segundos + 1) }
            }
        }
    }

    private fun actualizarCronometro(cambio: (Cronometro) -> Cronometro) {
        _uiState.update { it.copy(cronometro = cambio(it.cronometro)) }
    }

    private companion object {
        const val ESPERA_AL_TERMINAR_MS = 2_000L
    }
}
