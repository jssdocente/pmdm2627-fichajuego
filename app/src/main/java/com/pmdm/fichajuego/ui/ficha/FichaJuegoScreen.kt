package com.pmdm.fichajuego.ui.ficha

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pmdm.fichajuego.model.Juego
import com.pmdm.fichajuego.model.juegoEjemplo
import com.pmdm.fichajuego.ui.theme.FichaJuegoTheme

/**
 * Contenedor *stateful* de una ficha: guarda el favorito, que solo le interesa a ella.
 *
 * Las partidas llegan desde fuera porque BibliotecaScreen necesita sumarlas
 * (el estado sube hasta el antepasado común que lo usa).
 */
@Composable
fun FichaJuegoScreen(
    juego: Juego,
    partidas: Int,
    onSumarPartida: () -> Unit,
    modifier: Modifier = Modifier
) {
    var esFavorito by rememberSaveable { mutableStateOf(false) }

    FichaJuego(
        juego = juego,
        esFavorito = esFavorito,
        partidas = partidas,
        onFavoritoClick = { esFavorito = !esFavorito },
        onSumarPartida = onSumarPartida,
        modifier = modifier
    )
}

// Con el modo interactivo de la preview se puede pulsar el favorito
@Preview(showBackground = true)
@Composable
private fun FichaJuegoScreenPreview() {
    FichaJuegoTheme(dynamicColor = false) {
        FichaJuegoScreen(
            juego = juegoEjemplo,
            partidas = 0,
            onSumarPartida = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
