package com.pmdm.fichajuego.data

import com.pmdm.fichajuego.model.catalogo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests del repositorio: se ejecutan en el ordenador, sin emulador ni pantallas.
 * Son los tests de FichaJuegoViewModel de la sesión 3.05: la lógica se ha mudado aquí.
 */
class JuegoRepositorioTest {

    @Test
    fun `al empezar tiene el catalogo y ninguna partida`() {
        val repositorio = JuegoRepositorio()

        assertEquals(catalogo, repositorio.juegos.value)
        assertTrue(repositorio.partidas.value.isEmpty())
    }

    @Test
    fun `sumar partida incrementa solo el contador de ese juego`() {
        val repositorio = JuegoRepositorio()

        repositorio.sumarPartida(juegoId = 4)
        repositorio.sumarPartida(juegoId = 4)
        repositorio.sumarPartida(juegoId = 1)

        assertEquals(mapOf(4 to 2, 1 to 1), repositorio.partidas.value)
    }

    @Test
    fun `reiniciar deja todas las partidas a cero`() {
        val repositorio = JuegoRepositorio()
        repositorio.sumarPartida(juegoId = 4)

        repositorio.reiniciarPartidas()

        assertTrue(repositorio.partidas.value.isEmpty())
    }

    @Test
    fun `el juego anadido aparece el primero, con un id nuevo y marcado como novedad`() {
        val repositorio = JuegoRepositorio()

        repositorio.anadirJuego("Tunic", "Aventura", 2022, 4.5)

        val juegos = repositorio.juegos.value
        val nuevo = juegos.first()
        assertEquals("Tunic", nuevo.titulo)
        assertEquals(catalogo.maxOf { it.id } + 1, nuevo.id)
        assertTrue(nuevo.esNovedad)
        assertEquals(catalogo.size + 1, juegos.size)
    }
}
