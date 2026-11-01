package com.pmdm.fichajuego.ui.detalle

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pmdm.fichajuego.R
import com.pmdm.fichajuego.ui.theme.FichaJuegoTheme

/**
 * Cronómetro de la sesión de juego. Es *stateless*: no sabe nada de corrutinas ni de Job.
 * Pinta lo que le dicen y avisa de los clics; las reglas viven en el ViewModel.
 */
@Composable
fun TarjetaCronometro(
    cronometro: Cronometro,
    onEmpezar: () -> Unit,
    onPausar: () -> Unit,
    onTerminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val enMarcha = cronometro.enMarcha
    val guardando = cronometro.guardando
    val segundos = cronometro.segundos

    // El color del tiempo sigue al estado, igual que el favorito de la ficha
    val colorTiempo by animateColorAsState(
        targetValue = if (enMarcha) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "colorTiempo"
    )

    ElevatedCard(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.crono_tiempo, segundos / 60, segundos % 60),
                style = MaterialTheme.typography.displaySmall,
                color = colorTiempo
            )
            if (guardando) {
                Text(stringResource(R.string.crono_guardando))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = if (enMarcha) onPausar else onEmpezar,
                    // Bloqueo: durante la espera no se puede pulsar
                    enabled = !guardando
                ) {
                    Text(
                        stringResource(
                            when {
                                enMarcha -> R.string.crono_pausar
                                segundos > 0 -> R.string.crono_continuar
                                else -> R.string.crono_empezar
                            }
                        )
                    )
                }
                OutlinedButton(
                    onClick = onTerminar,
                    enabled = (enMarcha || segundos > 0) && !guardando
                ) {
                    Text(stringResource(R.string.crono_terminar))
                }
            }
        }
    }
}

@Preview(name = "Sin empezar", showBackground = true)
@Composable
private fun TarjetaCronometroParadoPreview() {
    FichaJuegoTheme {
        TarjetaCronometro(Cronometro(), onEmpezar = {}, onPausar = {}, onTerminar = {})
    }
}

@Preview(name = "En marcha", showBackground = true)
@Composable
private fun TarjetaCronometroEnMarchaPreview() {
    FichaJuegoTheme {
        TarjetaCronometro(
            Cronometro(segundos = 75, enMarcha = true),
            onEmpezar = {}, onPausar = {}, onTerminar = {}
        )
    }
}

@Preview(name = "Guardando", showBackground = true)
@Composable
private fun TarjetaCronometroGuardandoPreview() {
    FichaJuegoTheme {
        TarjetaCronometro(
            Cronometro(segundos = 75, guardando = true),
            onEmpezar = {}, onPausar = {}, onTerminar = {}
        )
    }
}
