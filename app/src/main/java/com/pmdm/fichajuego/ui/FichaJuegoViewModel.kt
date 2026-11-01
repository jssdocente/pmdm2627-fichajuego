package com.pmdm.fichajuego.ui

import androidx.lifecycle.ViewModel
import com.pmdm.fichajuego.R
import com.pmdm.fichajuego.model.Juego
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
}
