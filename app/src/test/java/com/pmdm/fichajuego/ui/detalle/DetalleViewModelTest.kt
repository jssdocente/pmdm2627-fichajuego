package com.pmdm.fichajuego.ui.detalle

import com.pmdm.fichajuego.data.JuegoRepositorio
import com.pmdm.fichajuego.ui.MainDispatcherRule
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * El cronómetro, ahora en el ViewModel del detalle. Mismos tests que en la sesión 3.06, con una
 * diferencia: uiState se deriva con stateIn y WhileSubscribed, así que necesita un suscriptor.
 */
class DetalleViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repositorio = JuegoRepositorio()
    // by lazy: se crea dentro del test, cuando la regla ya ha cambiado el hilo principal
    private val viewModel by lazy { DetalleViewModel(juegoId = 1, repositorio) }
    private val cronometro get() = viewModel.uiState.value.cronometro

    private fun TestScope.observar() {
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) { viewModel.uiState.collect {} }
    }

    private fun TestScope.avanzar(ms: Long) {
        advanceTimeBy(ms)
        runCurrent()
    }

    @Test
    fun `al empezar cuenta y empezar dos veces no lanza dos cronometros`() = runTest {
        observar()
        viewModel.onEmpezar()
        viewModel.onEmpezar()

        avanzar(3_000)

        assertEquals(3, cronometro.segundos)
        viewModel.onPausar()
    }

    @Test
    fun `terminar bloquea durante la espera y despues suma la partida`() = runTest {
        observar()
        viewModel.onEmpezar()
        avanzar(1_000)

        viewModel.onTerminar()
        avanzar(0)
        assertTrue(cronometro.guardando)
        assertEquals(0, viewModel.uiState.value.partidas)

        avanzar(2_000)
        assertEquals(1, viewModel.uiState.value.partidas)
        assertEquals(Cronometro(), cronometro)
    }

    @Test
    fun `al salir de la app se para y al volver continua`() = runTest {
        observar()
        viewModel.onEmpezar()
        avanzar(2_000)

        viewModel.onSalirDeLaApp()
        avanzar(10_000)
        assertEquals(2, cronometro.segundos)
        assertTrue(cronometro.enMarcha)

        viewModel.onVolverALaApp()
        avanzar(1_000)
        assertEquals(3, cronometro.segundos)

        viewModel.onPausar()
        avanzar(0)
        assertFalse(cronometro.enMarcha)
    }
}
