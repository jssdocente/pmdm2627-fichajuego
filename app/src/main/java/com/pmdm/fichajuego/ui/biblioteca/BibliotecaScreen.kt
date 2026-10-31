package com.pmdm.fichajuego.ui.biblioteca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.pmdm.fichajuego.model.catalogo
import com.pmdm.fichajuego.ui.ficha.FichaJuegoScreen
import com.pmdm.fichajuego.ui.theme.FichaJuegoTheme

/**
 * Pantalla principal: barra superior + lista perezosa con todo el catálogo.
 *
 * Las partidas de cada juego viven aquí, en un mapa id → partidas, porque el total las necesita.
 * El favorito sigue dentro de cada FichaJuegoScreen: por eso la lista necesita key.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BibliotecaScreen(modifier: Modifier = Modifier) {
    var partidas by rememberSaveable { mutableStateOf(mapOf<Int, Int>()) }
    var ordenarPorNota by rememberSaveable { mutableStateOf(false) }

    val juegos = if (ordenarPorNota) catalogo.sortedByDescending { it.nota } else catalogo

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
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        // innerPadding: el hueco de la barra superior y de las barras del sistema
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = stringResource(R.string.partidas_total, partidas.values.sum()),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            // key: cada fila se identifica por el id del juego, no por su posición
            items(items = juegos, key = { juego -> juego.id }) { juego ->
                FichaJuegoScreen(
                    juego = juego,
                    partidas = partidas[juego.id] ?: 0,
                    onSumarPartida = {
                        partidas = partidas + (juego.id to (partidas[juego.id] ?: 0) + 1)
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun BibliotecaScreenPreview() {
    FichaJuegoTheme {
        BibliotecaScreen()
    }
}
