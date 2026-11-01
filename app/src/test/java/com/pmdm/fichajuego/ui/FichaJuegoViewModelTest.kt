package com.pmdm.fichajuego.ui

import com.pmdm.fichajuego.model.catalogo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests del ViewModel: se ejecutan en el ordenador, sin emulador ni pantallas.
 * Cada test crea su propio ViewModel, llama a un evento y comprueba el estado.
 */
class FichaJuegoViewModelTest {

    @Test
    fun `al empezar muestra el catalogo y ninguna partida`() {
        val viewModel = FichaJuegoViewModel()

        val estado = viewModel.uiState.value
        assertEquals(catalogo, estado.juegos)
        assertEquals(0, estado.totalPartidas)
    }

    @Test
    fun `sumar partida incrementa solo el contador de ese juego`() {
        val viewModel = FichaJuegoViewModel()

        viewModel.onSumarPartida(juegoId = 4)
        viewModel.onSumarPartida(juegoId = 4)
        viewModel.onSumarPartida(juegoId = 1)

        val estado = viewModel.uiState.value
        assertEquals(2, estado.partidasDe(4))
        assertEquals(1, estado.partidasDe(1))
        assertEquals(0, estado.partidasDe(2))
        assertEquals(3, estado.totalPartidas)
    }

    @Test
    fun `reiniciar deja todas las partidas a cero`() {
        val viewModel = FichaJuegoViewModel()
        viewModel.onSumarPartida(juegoId = 4)

        viewModel.onReiniciarPartidas()

        assertTrue(viewModel.uiState.value.partidas.isEmpty())
    }

    @Test
    fun `el juego anadido aparece el primero, con un id nuevo y marcado como novedad`() {
        val viewModel = FichaJuegoViewModel()

        viewModel.onAnadirJuego("Tunic", "Aventura", 2022, 4.5)

        val estado = viewModel.uiState.value
        val nuevo = estado.juegos.first()
        assertEquals("Tunic", nuevo.titulo)
        assertEquals(catalogo.maxOf { it.id } + 1, nuevo.id)
        assertTrue(nuevo.esNovedad)
        assertEquals(catalogo.size + 1, estado.juegos.size)
    }
}
