package com.pmdm.fichajuego.ui.biblioteca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pmdm.fichajuego.model.juegoEjemplo
import com.pmdm.fichajuego.model.otroJuego
import com.pmdm.fichajuego.ui.ficha.FichaJuegoScreen
import com.pmdm.fichajuego.ui.theme.FichaJuegoTheme

/**
 * Pantalla con dos fichas y el total de partidas.
 *
 * Como el total necesita las partidas de ambas fichas, ese estado vive aquí,
 * en el padre común, y baja a cada ficha como parámetro.
 */
@Composable
fun BibliotecaScreen(modifier: Modifier = Modifier) {
    var partidasHollow by rememberSaveable { mutableIntStateOf(0) }
    var partidasStardew by rememberSaveable { mutableIntStateOf(0) }

    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Partidas en total: ${partidasHollow + partidasStardew}",
            style = MaterialTheme.typography.titleLarge
        )
        FichaJuegoScreen(
            juego = juegoEjemplo,
            partidas = partidasHollow,
            onSumarPartida = { partidasHollow++ }
        )
        FichaJuegoScreen(
            juego = otroJuego,
            partidas = partidasStardew,
            onSumarPartida = { partidasStardew++ }
        )
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun BibliotecaScreenPreview() {
    FichaJuegoTheme(dynamicColor = false) {
        BibliotecaScreen(modifier = Modifier.padding(16.dp))
    }
}
