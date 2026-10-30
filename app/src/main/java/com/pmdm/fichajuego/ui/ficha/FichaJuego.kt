package com.pmdm.fichajuego.ui.ficha

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pmdm.fichajuego.model.Juego
import com.pmdm.fichajuego.model.juegoEjemplo
import com.pmdm.fichajuego.model.otroJuego
import com.pmdm.fichajuego.ui.theme.FichaJuegoTheme

/**
 * Ficha completa de un juego.
 *
 * Árbol: Column → [Portada (Box), Text, Text, FilaValoracion (Row)]
 */
@Composable
fun FichaJuego(juego: Juego, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Portada(titulo = juego.titulo, esNovedad = juego.esNovedad)
        Text(
            text = juego.titulo,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "${juego.genero} · ${juego.anio}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        FilaValoracion(nota = juego.nota)
    }
}

/**
 * Portada provisional: un rectángulo de color con las iniciales del juego.
 * La insignia «NUEVO» se dibuja encima gracias a Box.
 */
@Composable
fun Portada(titulo: String, esNovedad: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = iniciales(titulo),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier.align(Alignment.Center)
        )
        // UI = f(datos): la insignia solo existe si el juego es una novedad
        if (esNovedad) {
            Text(
                text = "NUEVO",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(MaterialTheme.colorScheme.tertiary, RoundedCornerShape(8.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}

/**
 * Nota a la izquierda y favorito a la derecha: el Spacer con weight ocupa el hueco central.
 */
@Composable
fun FilaValoracion(nota: Double, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "★ $nota", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "♡ Favorito", style = MaterialTheme.typography.labelLarge)
    }
}

private fun iniciales(titulo: String): String =
    titulo.split(" ").take(2).joinToString("") { it.first().uppercase() }

@Preview(name = "Claro", showBackground = true)
@Composable
private fun FichaJuegoPreview() {
    FichaJuegoTheme(dynamicColor = false) {
        FichaJuego(juego = juegoEjemplo, modifier = Modifier.padding(16.dp))
    }
}

@Preview(name = "Oscuro", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FichaJuegoOscuroPreview() {
    FichaJuegoTheme(darkTheme = true, dynamicColor = false) {
        FichaJuego(juego = juegoEjemplo, modifier = Modifier.padding(16.dp))
    }
}

@Preview(name = "Sin novedad", showBackground = true, widthDp = 320)
@Composable
private fun FichaJuegoSinNovedadPreview() {
    FichaJuegoTheme(dynamicColor = false) {
        FichaJuego(juego = otroJuego, modifier = Modifier.padding(16.dp))
    }
}
