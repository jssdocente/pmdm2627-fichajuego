package com.pmdm.fichajuego.ui.biblioteca

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pmdm.fichajuego.R
import com.pmdm.fichajuego.ui.theme.FichaJuegoTheme
import java.time.Year
import kotlin.math.roundToInt

private const val MAX_TITULO = 40
private const val ANIO_MINIMO = 1970

/**
 * Formulario para añadir un juego a la biblioteca.
 *
 * Lo que se va escribiendo es estado del propio formulario: a nadie más le interesa
 * hasta que se pulsa «Añadir». Entonces se entrega todo junto con onGuardar.
 */
@Composable
fun FormularioNuevoJuego(
    onGuardar: (titulo: String, genero: String, anio: Int, nota: Double) -> Unit,
    onCancelar: () -> Unit,
    modifier: Modifier = Modifier
) {
    var titulo by rememberSaveable { mutableStateOf("") }
    var genero by rememberSaveable { mutableStateOf("") }
    var anioTexto by rememberSaveable { mutableStateOf("") }
    var nota by rememberSaveable { mutableFloatStateOf(3f) }

    // Validación: se recalcula en cada recomposición, no se guarda en ningún estado
    val anioActual = Year.now().value
    val anio = anioTexto.toIntOrNull()
    val anioValido = anio != null && anio in ANIO_MINIMO..anioActual
    val mostrarErrorAnio = anioTexto.isNotEmpty() && !anioValido
    val formularioValido = titulo.isNotBlank() && genero.isNotBlank() && anioValido

    Column(
        modifier = modifier
            .fillMaxWidth()
            // Con el teclado abierto no cabe todo: el formulario se desplaza
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.titulo_nuevo_juego),
            style = MaterialTheme.typography.titleLarge
        )

        OutlinedTextField(
            value = titulo,
            // Si se pasa del máximo, ignoramos la tecla: el estado no cambia
            onValueChange = { if (it.length <= MAX_TITULO) titulo = it },
            label = { Text(stringResource(R.string.campo_titulo)) },
            supportingText = {
                Text(stringResource(R.string.contador_caracteres, titulo.length, MAX_TITULO))
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = genero,
            onValueChange = { genero = it },
            label = { Text(stringResource(R.string.campo_genero)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = anioTexto,
            onValueChange = { anioTexto = it },
            label = { Text(stringResource(R.string.campo_anio)) },
            isError = mostrarErrorAnio,
            supportingText = {
                if (mostrarErrorAnio) {
                    Text(stringResource(R.string.error_anio, ANIO_MINIMO, anioActual))
                } else {
                    Text(stringResource(R.string.ayuda_anio))
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Text(stringResource(R.string.campo_nota, nota))
        Slider(
            value = nota,
            onValueChange = { nota = it },
            valueRange = 0f..5f,
            steps = 9 // 9 puntos intermedios: de 0 a 5 de medio en medio
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)
        ) {
            TextButton(onClick = onCancelar) {
                Text(stringResource(R.string.cancelar))
            }
            Button(
                onClick = {
                    // anio no es null: el botón solo está activo si el formulario es válido
                    onGuardar(titulo.trim(), genero.trim(), anio!!, (nota * 2).roundToInt() / 2.0)
                },
                enabled = formularioValido
            ) {
                Text(stringResource(R.string.anadir_juego))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FormularioNuevoJuegoPreview() {
    FichaJuegoTheme {
        FormularioNuevoJuego(onGuardar = { _, _, _, _ -> }, onCancelar = {})
    }
}
