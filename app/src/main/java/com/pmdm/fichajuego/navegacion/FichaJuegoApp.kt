package com.pmdm.fichajuego.navegacion

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.pmdm.fichajuego.R
import com.pmdm.fichajuego.model.Juego
import com.pmdm.fichajuego.model.catalogo
import com.pmdm.fichajuego.ui.biblioteca.BibliotecaScreen
import com.pmdm.fichajuego.ui.detalle.DetalleJuegoScreen

/**
 * Raíz de la app: la pila de navegación y los datos que comparten las pantallas.
 *
 * Los juegos y las partidas suben hasta aquí porque los usan la biblioteca y el detalle,
 * y porque este composable no sale nunca de la composición (las pantallas sí).
 * Es el único sitio que toca la pila: las pantallas solo reciben lambdas.
 */
@Composable
fun FichaJuegoApp() {
    val pila = rememberNavBackStack(Biblioteca)

    // remember: al girar se pierden los juegos añadidos (lo resolveremos con un ViewModel)
    var juegos by remember { mutableStateOf(catalogo) }
    var partidas by rememberSaveable { mutableStateOf(mapOf<Int, Int>()) }

    fun sumarPartida(id: Int) {
        partidas = partidas + (id to (partidas[id] ?: 0) + 1)
    }

    NavDisplay(
        backStack = pila,
        onBack = { pila.removeLastOrNull() },
        entryProvider = entryProvider {
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
                    onJuegoClick = { id -> pila.add(DetalleJuego(juegoId = id)) }
                )
            }
            // La clave trae los argumentos: clave.juegoId
            entry<DetalleJuego> { clave ->
                DetalleJuegoScreen(
                    juego = juegos.find { it.id == clave.juegoId },
                    partidas = partidas[clave.juegoId] ?: 0,
                    onSumarPartida = { sumarPartida(clave.juegoId) },
                    onVolver = { pila.removeLastOrNull() }
                )
            }
        }
    )
}
