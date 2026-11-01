package com.pmdm.fichajuego.ui.estadisticas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pmdm.fichajuego.R
import com.pmdm.fichajuego.model.Juego
import com.pmdm.fichajuego.model.catalogo
import com.pmdm.fichajuego.ui.theme.FichaJuegoTheme
import java.util.Locale

/**
 * Resumen de la biblioteca. No guarda nada: todo se calcula a partir de los juegos
 * y las partidas que recibe, así que siempre está al día.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadisticasScreen(
    juegos: List<Juego>,
    partidas: Map<Int, Int>,
    onJuegoClick: (juegoId: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalPartidas = partidas.values.sum()
    val notaMedia = juegos.map { it.nota }.average()
    // El más jugado: solo si hay alguna partida
    val masJugado = juegos
        .filter { (partidas[it.id] ?: 0) > 0 }
        .maxByOrNull { partidas[it.id] ?: 0 }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.pestana_estadisticas)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                ListItem(
                    overlineContent = { Text(stringResource(R.string.est_partidas)) },
                    headlineContent = { Text("$totalPartidas", style = MaterialTheme.typography.headlineSmall) }
                )
                ListItem(
                    overlineContent = { Text(stringResource(R.string.est_juegos)) },
                    headlineContent = { Text("${juegos.size}", style = MaterialTheme.typography.headlineSmall) }
                )
                ListItem(
                    overlineContent = { Text(stringResource(R.string.est_nota_media)) },
                    headlineContent = {
                        Text(String.format(Locale.getDefault(), "%.2f", notaMedia), style = MaterialTheme.typography.headlineSmall)
                    }
                )
            }

            Text(stringResource(R.string.est_mas_jugado), style = MaterialTheme.typography.titleMedium)
            if (masJugado == null) {
                Text(
                    text = stringResource(R.string.est_sin_partidas),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                // Al pulsarlo se abre su detalle... dentro de la pestaña de estadísticas
                ElevatedCard(onClick = { onJuegoClick(masJugado.id) }, modifier = Modifier.fillMaxWidth()) {
                    ListItem(
                        headlineContent = { Text(masJugado.titulo) },
                        supportingContent = {
                            Text(stringResource(R.string.partidas_jugadas, partidas[masJugado.id] ?: 0))
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, heightDp = 640)
@Composable
private fun EstadisticasScreenPreview() {
    FichaJuegoTheme {
        EstadisticasScreen(juegos = catalogo, partidas = mapOf(1 to 3, 4 to 5), onJuegoClick = {})
    }
}
