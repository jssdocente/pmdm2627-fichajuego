package com.pmdm.fichajuego.ui

import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Tests con tiempo virtual: tres segundos de delay no tardan tres segundos.
 *
 * Cada test que arranca el cronómetro lo para al final. El cronómetro es un bucle infinito:
 * advanceUntilIdle() nunca terminaría, porque siempre hay otro delay pendiente.
 */
class CronometroTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val viewModel = FichaJuegoViewModel()
    private val cronometro get() = viewModel.uiState.value.cronometro

    // advanceTimeBy no ejecuta lo programado justo en ese instante: runCurrent sí
    private fun TestScope.avanzar(ms: Long) {
        advanceTimeBy(ms)
        runCurrent()
    }

    @Test
    fun `al empezar cuenta un segundo por cada segundo de tiempo`() = runTest {
        viewModel.onEmpezar(juegoId = 1)

        avanzar(3_000)

        assertEquals(3, cronometro.segundos)
        assertTrue(cronometro.enMarcha)
        viewModel.onPausar()
    }

    @Test
    fun `pausar detiene el tiempo y continuar sigue desde donde iba`() = runTest {
        viewModel.onEmpezar(juegoId = 1)
        avanzar(2_000)

        viewModel.onPausar()
        avanzar(5_000)
        assertEquals(2, cronometro.segundos)

        viewModel.onEmpezar(juegoId = 1)
        avanzar(1_000)
        assertEquals(3, cronometro.segundos)
        viewModel.onPausar()
    }

    @Test
    fun `empezar dos veces no lanza dos cronometros`() = runTest {
        viewModel.onEmpezar(juegoId = 1)
        viewModel.onEmpezar(juegoId = 1)

        avanzar(3_000)

        assertEquals(3, cronometro.segundos)
        viewModel.onPausar()
    }

    @Test
    fun `no se puede empezar otro juego con una sesion abierta`() = runTest {
        viewModel.onEmpezar(juegoId = 1)
        viewModel.onPausar()

        viewModel.onEmpezar(juegoId = 2)

        assertEquals(1, cronometro.juegoId)
        assertFalse(cronometro.enMarcha)
    }

    @Test
    fun `terminar bloquea durante la espera y despues suma la partida`() = runTest {
        viewModel.onEmpezar(juegoId = 1)
        avanzar(1_000)

        viewModel.onTerminar()
        assertTrue(cronometro.guardando)
        assertEquals(0, viewModel.uiState.value.partidasDe(1))

        // Durante la espera, empezar no hace nada
        viewModel.onEmpezar(juegoId = 1)
        assertFalse(cronometro.enMarcha)

        avanzar(2_000)
        assertEquals(1, viewModel.uiState.value.partidasDe(1))
        assertEquals(Cronometro(), cronometro)
    }

    @Test
    fun `al salir de la app se para y al volver continua`() = runTest {
        viewModel.onEmpezar(juegoId = 1)
        avanzar(2_000)

        viewModel.onSalirDeLaApp()
        avanzar(10_000)
        assertEquals(2, cronometro.segundos)

        viewModel.onVolverALaApp()
        avanzar(1_000)
        assertEquals(3, cronometro.segundos)
        viewModel.onPausar()
    }

    @Test
    fun `al volver a la app no arranca si el usuario lo habia pausado`() = runTest {
        viewModel.onEmpezar(juegoId = 1)
        avanzar(1_000)
        viewModel.onPausar()

        viewModel.onSalirDeLaApp()
        viewModel.onVolverALaApp()
        avanzar(5_000)

        assertEquals(1, cronometro.segundos)
    }
}
