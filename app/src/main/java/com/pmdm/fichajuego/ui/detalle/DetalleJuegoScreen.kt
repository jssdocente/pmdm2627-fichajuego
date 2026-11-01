package com.pmdm.fichajuego.ui.detalle

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pmdm.fichajuego.R
import com.pmdm.fichajuego.model.Juego
import com.pmdm.fichajuego.model.juegoEjemplo
import com.pmdm.fichajuego.ui.ficha.FilaPartidas
import com.pmdm.fichajuego.ui.ficha.Portada
import com.pmdm.fichajuego.ui.theme.FichaJuegoTheme

/**
 * Detalle de un juego. No conoce la pila de navegación: recibe el juego ya buscado
 * y avisa con onVolver cuando se pulsa la flecha. Así se puede previsualizar.
 *
 * juego es nullable: el id que llega en la clave podría no existir.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleJuegoScreen(
    juego: Juego?,
    partidas: Int,
    onSumarPartida: () -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(juego?.titulo ?: stringResource(R.string.juego_no_encontrado)) },
                navigationIcon = {
                    IconButton(onClick = onVolver) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_back),
                            contentDescription = stringResource(R.string.volver)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        if (juego == null) return@Scaffold

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // El primer modificador de tamaño manda: la portada crece de 160 a 240 dp
            Portada(
                portada = juego.portada,
                titulo = juego.titulo,
                esNovedad = juego.esNovedad,
                modifier = Modifier.height(240.dp)
            )
            Text(text = juego.titulo, style = MaterialTheme.typography.headlineSmall)
            ListItem(
                overlineContent = { Text(stringResource(R.string.detalle_genero)) },
                headlineContent = { Text(juego.genero) }
            )
            ListItem(
                overlineContent = { Text(stringResource(R.string.detalle_anio)) },
                headlineContent = { Text("${juego.anio}") }
            )
            ListItem(
                overlineContent = { Text(stringResource(R.string.detalle_nota)) },
                headlineContent = { Text("${juego.nota}") }
            )
            // Reutilizamos la fila de la ficha: el contador es el mismo que en la lista
            FilaPartidas(partidas = partidas, onSumarPartida = onSumarPartida)
        }
    }
}

@Preview(showBackground = true, heightDp = 800)
@Composable
private fun DetalleJuegoScreenPreview() {
    FichaJuegoTheme {
        DetalleJuegoScreen(juego = juegoEjemplo, partidas = 3, onSumarPartida = {}, onVolver = {})
    }
}

@Preview(showBackground = true)
@Composable
private fun DetalleJuegoNoEncontradoPreview() {
    FichaJuegoTheme {
        DetalleJuegoScreen(juego = null, partidas = 0, onSumarPartida = {}, onVolver = {})
    }
}
