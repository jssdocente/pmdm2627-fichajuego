package com.pmdm.fichajuego.navegacion

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.pmdm.fichajuego.ui.biblioteca.BibliotecaScreen
import com.pmdm.fichajuego.ui.detalle.DetalleJuegoScreen
import com.pmdm.fichajuego.FichaJuegoApplication
import com.pmdm.fichajuego.ui.biblioteca.BibliotecaViewModel
import com.pmdm.fichajuego.ui.detalle.DetalleViewModel
import com.pmdm.fichajuego.ui.estadisticas.EstadisticasScreen
import com.pmdm.fichajuego.ui.estadisticas.EstadisticasViewModel

/**
 * Raíz de la app: la barra inferior, una pila por pestaña y un ViewModel por pantalla.
 *
 * Los datos viven en el repositorio, que se crea una sola vez en FichaJuegoApplication.
 * Cada entrada crea el ViewModel de su pantalla pasándole el repositorio.
 * Es el único sitio que toca las pilas: las pantallas solo reciben lambdas.
 */
@Composable
fun FichaJuegoApp() {
    // La misma instancia para toda la app: la crea FichaJuegoApplication al arrancar
    val repositorio = (LocalContext.current.applicationContext as FichaJuegoApplication).repositorio

    var seleccionada by rememberSaveable { mutableStateOf(Pestana.Inicio) }

    // Una pila independiente para cada pestaña: así cada una recuerda dónde estaba
    val pilas = Pestana.entries.associateWith { rememberNavBackStack(it.raiz) }
    val pilaActual = pilas.getValue(seleccionada)

    fun navegar(destino: NavKey) {
        pilaActual.add(destino)
    }

    // «Salir por inicio»: dentro de una pestaña se vuelve atrás en su pila;
    // desde la raíz de otra pestaña se vuelve a Inicio, y desde Inicio se sale de la app
    fun volver() {
        if (pilaActual.size > 1) {
            pilaActual.removeLastOrNull()
        } else {
            seleccionada = Pestana.Inicio
        }
    }

    val proveedor = entryProvider {
        entry<Biblioteca> {
            // viewModel { }: la primera vez lo crea con esta lambda; después devuelve el mismo
            val viewModel = viewModel { BibliotecaViewModel(repositorio) }
            val estado by viewModel.uiState.collectAsStateWithLifecycle()
            BibliotecaScreen(
                juegos = estado.juegos,
                partidas = estado.partidas,
                onSumarPartida = viewModel::onSumarPartida,
                onReiniciarPartidas = viewModel::onReiniciarPartidas,
                onAnadirJuego = viewModel::onAnadirJuego,
                onJuegoClick = { id -> navegar(DetalleJuego(juegoId = id)) }
            )
        }
        entry<Estadisticas> {
            val viewModel = viewModel { EstadisticasViewModel(repositorio) }
            val estado by viewModel.uiState.collectAsStateWithLifecycle()
            EstadisticasScreen(
                estado = estado,
                onJuegoClick = { id -> navegar(DetalleJuego(juegoId = id)) }
            )
        }
        // La clave trae los argumentos: clave.juegoId
        entry<DetalleJuego> { clave ->
            // Cada detalle abierto tiene SU ViewModel, creado con el id de su clave
            val viewModel = viewModel { DetalleViewModel(clave.juegoId, repositorio) }
            val estado by viewModel.uiState.collectAsStateWithLifecycle()
            // Al salir con el botón de inicio el cronómetro se para, y al volver continúa.
            // Se ejecuta al entrar en estado RESUMED y onPauseOrDispose, al salir de él
            LifecycleResumeEffect(viewModel) {
                viewModel.onVolverALaApp()
                onPauseOrDispose { viewModel.onSalirDeLaApp() }
            }
            DetalleJuegoScreen(
                juego = estado.juego,
                partidas = estado.partidas,
                cronometro = estado.cronometro,
                onSumarPartida = viewModel::onSumarPartida,
                onEmpezar = viewModel::onEmpezar,
                onPausar = viewModel::onPausar,
                onTerminar = viewModel::onTerminar,
                onVolver = ::volver
            )
        }
    }

    // Cada pila con SUS PROPIOS decoradores:
    // 1. conserva el rememberSaveable de sus pantallas aunque su pestaña no se muestre
    // 2. da a cada entrada su propio almacén de ViewModels, que se vacía al sacarla de la pila
    val entradasPorPestana = pilas.mapValues { (_, pila) ->
        rememberDecoratedNavEntries(
            backStack = pila,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = proveedor
        )
    }

    // Debajo siempre está Inicio; encima, la pestaña seleccionada si es otra
    val entradasVisibles =
        if (seleccionada == Pestana.Inicio) entradasPorPestana.getValue(Pestana.Inicio)
        else entradasPorPestana.getValue(Pestana.Inicio) + entradasPorPestana.getValue(seleccionada)

    Scaffold(
        // Este Scaffold solo pone la barra inferior: la parte de arriba (barra de estado)
        // la gestiona cada pantalla, así el detalle puede dibujar su portada bajo ella
        contentWindowInsets = WindowInsets(0),
        bottomBar = {
            NavigationBar {
                Pestana.entries.forEach { pestana ->
                    NavigationBarItem(
                        selected = pestana == seleccionada,
                        onClick = { seleccionada = pestana },
                        icon = { Icon(painterResource(pestana.icono), contentDescription = null) },
                        label = { Text(stringResource(pestana.etiqueta)) }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            entries = entradasVisibles,
            onBack = ::volver,
            // consumeWindowInsets: avisa a los Scaffold de dentro de que la barra de
            // navegación ya está cubierta, para que no vuelvan a dejar su hueco
            modifier = Modifier
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
        )
    }
}
