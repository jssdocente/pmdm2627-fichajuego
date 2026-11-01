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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberDecoratedNavEntries
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.pmdm.fichajuego.R
import com.pmdm.fichajuego.model.Juego
import com.pmdm.fichajuego.model.catalogo
import com.pmdm.fichajuego.ui.biblioteca.BibliotecaScreen
import com.pmdm.fichajuego.ui.detalle.DetalleJuegoScreen
import com.pmdm.fichajuego.ui.estadisticas.EstadisticasScreen

/**
 * Raíz de la app: la barra inferior, una pila por pestaña y los datos que comparten las pantallas.
 *
 * Los juegos y las partidas suben hasta aquí porque los usan varias pantallas,
 * y porque este composable no sale nunca de la composición (las pantallas sí).
 * Es el único sitio que toca las pilas: las pantallas solo reciben lambdas.
 */
@Composable
fun FichaJuegoApp() {
    var seleccionada by rememberSaveable { mutableStateOf(Pestana.Inicio) }

    // Una pila independiente para cada pestaña: así cada una recuerda dónde estaba
    val pilas = Pestana.entries.associateWith { rememberNavBackStack(it.raiz) }
    val pilaActual = pilas.getValue(seleccionada)

    // remember: al girar se pierden los juegos añadidos (lo resolveremos con un ViewModel)
    var juegos by remember { mutableStateOf(catalogo) }
    var partidas by rememberSaveable { mutableStateOf(mapOf<Int, Int>()) }

    fun sumarPartida(id: Int) {
        partidas = partidas + (id to (partidas[id] ?: 0) + 1)
    }

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
            BibliotecaScreen(
                juegos = juegos,
                partidas = partidas,
                onSumarPartida = ::sumarPartida,
                onReiniciarPartidas = { partidas = emptyMap() },
                onAnadirJuego = { titulo, genero, anio, nota ->
                    val nuevo = Juego(
                        id = (juegos.maxOfOrNull { it.id } ?: 0) + 1,
                        titulo = titulo,
                        genero = genero,
                        anio = anio,
                        nota = nota,
                        portada = R.drawable.img_portada_generica,
                        esNovedad = true
                    )
                    juegos = listOf(nuevo) + juegos
                },
                onJuegoClick = { id -> navegar(DetalleJuego(juegoId = id)) }
            )
        }
        entry<Estadisticas> {
            EstadisticasScreen(
                juegos = juegos,
                partidas = partidas,
                onJuegoClick = { id -> navegar(DetalleJuego(juegoId = id)) }
            )
        }
        // La clave trae los argumentos: clave.juegoId
        entry<DetalleJuego> { clave ->
            DetalleJuegoScreen(
                juego = juegos.find { it.id == clave.juegoId },
                partidas = partidas[clave.juegoId] ?: 0,
                onSumarPartida = { sumarPartida(clave.juegoId) },
                onVolver = ::volver
            )
        }
    }

    // Cada pila con SU PROPIO decorador: conserva el estado de sus pantallas
    // (rememberSaveable) aunque su pestaña no se esté mostrando
    val entradasPorPestana = pilas.mapValues { (_, pila) ->
        rememberDecoratedNavEntries(
            backStack = pila,
            entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator()),
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
