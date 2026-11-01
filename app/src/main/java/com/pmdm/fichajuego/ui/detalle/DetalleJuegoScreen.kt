package com.pmdm.fichajuego.ui.detalle

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pmdm.fichajuego.R
import com.pmdm.fichajuego.model.Juego
import com.pmdm.fichajuego.model.juegoEjemplo
import com.pmdm.fichajuego.ui.ficha.FilaPartidas
import com.pmdm.fichajuego.ui.theme.FichaJuegoTheme

/**
 * Detalle de un juego. No conoce la pila de navegación: recibe el juego ya buscado
 * y avisa con onVolver cuando se pulsa la flecha. Así se puede previsualizar.
 *
 * Es edge-to-edge sin Scaffold: la portada se dibuja también bajo la barra de estado,
 * y solo la flecha se aparta de ella con statusBarsPadding().
 * juego es nullable: el id que llega en la clave podría no existir.
 */
@Composable
fun DetalleJuegoScreen(
    juego: Juego?,
    partidas: Int,
    onSumarPartida: () -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Sin Scaffold, el fondo lo pintamos nosotros
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (juego != null) {
                Image(
                    painter = painterResource(juego.portada),
                    contentDescription = stringResource(R.string.portada_de, juego.titulo),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }
            // Velo con el color de fondo: la hora, la batería y la flecha se leen sobre cualquier portada
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        Brush.verticalGradient(
                            listOf(MaterialTheme.colorScheme.background.copy(alpha = 0.8f), Color.Transparent)
                        )
                    )
            )
            IconButton(
                onClick = onVolver,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(4.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.volver)
                )
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (juego == null) {
                Text(stringResource(R.string.juego_no_encontrado), style = MaterialTheme.typography.headlineSmall)
                return@Column
            }
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
