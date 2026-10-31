package com.pmdm.fichajuego.ui.ficha

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pmdm.fichajuego.R
import com.pmdm.fichajuego.model.Juego
import com.pmdm.fichajuego.model.juegoEjemplo
import com.pmdm.fichajuego.model.otroJuego
import com.pmdm.fichajuego.ui.theme.FichaJuegoTheme

/**
 * Ficha completa de un juego. Es *stateless*: recibe el estado como parámetros
 * (estado hacia abajo) y avisa de los clics con lambdas (eventos hacia arriba).
 *
 * Árbol: ElevatedCard → Column → [Portada (Box), Text, Text, FilaValoracion (Row), FilaPartidas (Row)]
 */
@Composable
fun FichaJuego(
    juego: Juego,
    esFavorito: Boolean,
    partidas: Int,
    onFavoritoClick: () -> Unit,
    onSumarPartida: () -> Unit,
    modifier: Modifier = Modifier
) {
    // ElevatedCard toma del tema el color (surfaceContainerLow), la sombra y la forma (shapes.medium)
    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Portada(portada = juego.portada, titulo = juego.titulo, esNovedad = juego.esNovedad)
            Text(
                text = juego.titulo,
                style = MaterialTheme.typography.headlineSmall // negrita definida en Type.kt
            )
            Text(
                text = stringResource(R.string.genero_anio, juego.genero, juego.anio),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            FilaValoracion(
                nota = juego.nota,
                esFavorito = esFavorito,
                onFavoritoClick = onFavoritoClick
            )
            FilaPartidas(partidas = partidas, onSumarPartida = onSumarPartida)
        }
    }
}

/**
 * Portada del juego: una imagen de res/drawable recortada para llenar el hueco.
 * La insignia «NUEVO» se dibuja encima gracias a Box.
 */
@Composable
fun Portada(
    @DrawableRes portada: Int,
    titulo: String,
    esNovedad: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(MaterialTheme.shapes.small)
    ) {
        Image(
            painter = painterResource(portada),
            contentDescription = stringResource(R.string.portada_de, titulo),
            contentScale = ContentScale.Crop, // llena el hueco y recorta lo que sobra
            modifier = Modifier.fillMaxSize()
        )
        // UI = f(datos): la insignia solo existe si el juego es una novedad
        if (esNovedad) {
            Text(
                text = stringResource(R.string.insignia_nuevo),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.tertiary, MaterialTheme.shapes.small)
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

/**
 * Nota a la izquierda y favorito a la derecha: el Spacer con weight ocupa el hueco central.
 */
@Composable
fun FilaValoracion(
    nota: Double,
    esFavorito: Boolean,
    onFavoritoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono decorativo: el número ya lo dice todo, la descripción la leería dos veces
        Icon(
            painter = painterResource(R.drawable.ic_star),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "$nota", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.weight(1f))
        IconToggleButton(checked = esFavorito, onCheckedChange = { onFavoritoClick() }) {
            Icon(
                painter = painterResource(
                    if (esFavorito) R.drawable.ic_favorite else R.drawable.ic_favorite_border
                ),
                contentDescription = stringResource(
                    if (esFavorito) R.string.quitar_favorito else R.string.marcar_favorito
                ),
                tint = if (esFavorito) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * Contador de partidas: muestra el valor que recibe y avisa cuando se pulsa «+1».
 */
@Composable
fun FilaPartidas(partidas: Int, onSumarPartida: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.partidas_jugadas, partidas),
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        FilledTonalButton(onClick = onSumarPartida) {
            Text(stringResource(R.string.sumar_partida))
        }
    }
}

@Preview(name = "Claro", showBackground = true)
@Composable
private fun FichaJuegoPreview() {
    FichaJuegoTheme(dynamicColor = false) {
        FichaJuego(
            juego = juegoEjemplo,
            esFavorito = false,
            partidas = 0,
            onFavoritoClick = {},
            onSumarPartida = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FichaJuegoOscuroPreview() {
    FichaJuegoTheme(darkTheme = true, dynamicColor = false) {
        FichaJuego(
            juego = juegoEjemplo,
            esFavorito = true,
            partidas = 12,
            onFavoritoClick = {},
            onSumarPartida = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(name = "Sin novedad, favorito", showBackground = true, widthDp = 320)
@Composable
private fun FichaJuegoSinNovedadPreview() {
    FichaJuegoTheme(dynamicColor = false) {
        FichaJuego(
            juego = otroJuego,
            esFavorito = true,
            partidas = 3,
            onFavoritoClick = {},
            onSumarPartida = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
