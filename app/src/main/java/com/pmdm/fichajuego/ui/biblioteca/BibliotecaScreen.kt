package com.pmdm.fichajuego.ui.biblioteca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pmdm.fichajuego.R
import com.pmdm.fichajuego.model.Juego
import com.pmdm.fichajuego.model.catalogo
import com.pmdm.fichajuego.ui.ficha.FichaJuegoScreen
import com.pmdm.fichajuego.ui.theme.FichaJuegoTheme

/**
 * Pantalla principal: barra superior + lista perezosa con todo el catálogo.
 *
 * Los juegos y las partidas llegan desde FichaJuegoApp, porque también los usa el detalle.
 * Aquí solo queda el estado de la propia pantalla: el orden, la hoja y el diálogo.
 * El favorito sigue dentro de cada FichaJuegoScreen: por eso la lista necesita key.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibliotecaScreen(
    juegos: List<Juego>,
    partidas: Map<Int, Int>,
    onSumarPartida: (juegoId: Int) -> Unit,
    onReiniciarPartidas: () -> Unit,
    onAnadirJuego: (titulo: String, genero: String, anio: Int, nota: Double) -> Unit,
    onJuegoClick: (juegoId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var ordenarPorNota by rememberSaveable { mutableStateOf(false) }
    var mostrarFormulario by rememberSaveable { mutableStateOf(false) }
    var confirmarReinicio by rememberSaveable { mutableStateOf(false) }

    val juegosVisibles = if (ordenarPorNota) juegos.sortedByDescending { it.nota } else juegos

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.titulo_biblioteca)) },
                actions = {
                    IconToggleButton(
                        checked = ordenarPorNota,
                        onCheckedChange = { ordenarPorNota = it }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_sort),
                            contentDescription = stringResource(
                                if (ordenarPorNota) R.string.quitar_orden else R.string.ordenar_por_nota
                            )
                        )
                    }
                    // Sin partidas no hay nada que reiniciar: el botón se desactiva
                    IconButton(
                        onClick = { confirmarReinicio = true },
                        enabled = partidas.isNotEmpty()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_restart),
                            contentDescription = stringResource(R.string.reiniciar_partidas)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { mostrarFormulario = true },
                icon = { Icon(painterResource(R.drawable.ic_add), contentDescription = null) },
                text = { Text(stringResource(R.string.anadir_juego)) }
            )
        }
    ) { innerPadding ->
        // innerPadding: el hueco de la barra superior y de las barras del sistema
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            // Abajo dejamos sitio extra para que el FAB no tape la última ficha
            contentPadding = PaddingValues(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 88.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.partidas_total, partidas.values.sum()),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            // key: cada fila se identifica por el id del juego, no por su posición
            items(items = juegosVisibles, key = { juego -> juego.id }) { juego ->
                FichaJuegoScreen(
                    juego = juego,
                    partidas = partidas[juego.id] ?: 0,
                    onSumarPartida = { onSumarPartida(juego.id) },
                    onClick = { onJuegoClick(juego.id) }
                )
            }
        }
    }

    // La hoja y el diálogo solo existen mientras su estado es true
    if (mostrarFormulario) {
        ModalBottomSheet(
            onDismissRequest = { mostrarFormulario = false },
            // Se abre entera: el formulario no cabe en media pantalla con el teclado abierto
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ) {
            FormularioNuevoJuego(
                // La hoja llega hasta el borde: sin esto, la barra de gestos y el teclado tapan el formulario
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding(),
                onGuardar = { titulo, genero, anio, nota ->
                    onAnadirJuego(titulo, genero, anio, nota)
                    mostrarFormulario = false
                },
                onCancelar = { mostrarFormulario = false }
            )
        }
    }

    if (confirmarReinicio) {
        DialogoReiniciarPartidas(
            onConfirmar = {
                onReiniciarPartidas()
                confirmarReinicio = false
            },
            onCancelar = { confirmarReinicio = false }
        )
    }
}

/** Diálogo de confirmación: no decide nada, solo avisa de lo que el usuario elige. */
@Composable
private fun DialogoReiniciarPartidas(
    onConfirmar: () -> Unit,
    onCancelar: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancelar,
        icon = { Icon(painterResource(R.drawable.ic_restart), contentDescription = null) },
        title = { Text(stringResource(R.string.reiniciar_titulo)) },
        text = { Text(stringResource(R.string.reiniciar_texto)) },
        confirmButton = {
            TextButton(onClick = onConfirmar) {
                Text(stringResource(R.string.reiniciar_confirmar))
            }
        },
        dismissButton = {
            TextButton(onClick = onCancelar) {
                Text(stringResource(R.string.cancelar))
            }
        }
    )
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun BibliotecaScreenPreview() {
    FichaJuegoTheme {
        BibliotecaScreen(
            juegos = catalogo,
            partidas = mapOf(1 to 3),
            onSumarPartida = {},
            onReiniciarPartidas = {},
            onAnadirJuego = { _, _, _, _ -> },
            onJuegoClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DialogoReiniciarPartidasPreview() {
    FichaJuegoTheme {
        DialogoReiniciarPartidas(onConfirmar = {}, onCancelar = {})
    }
}
