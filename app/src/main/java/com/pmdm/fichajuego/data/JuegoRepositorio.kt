package com.pmdm.fichajuego.data

import com.pmdm.fichajuego.R
import com.pmdm.fichajuego.model.Juego
import com.pmdm.fichajuego.model.catalogo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Los datos de la app: los juegos y las partidas jugadas a cada uno.
 *
 * Es la única fuente de verdad: todos los ViewModel observan esta misma instancia,
 * así que un cambio hecho desde una pantalla lo ven al momento todas las demás.
 * De momento guarda los datos en memoria; en la UT4 los guardará en una base de datos.
 */
class JuegoRepositorio {

    private val _juegos = MutableStateFlow(catalogo)
    val juegos: StateFlow<List<Juego>> = _juegos.asStateFlow()

    // Partidas jugadas por id de juego
    private val _partidas = MutableStateFlow(emptyMap<Int, Int>())
    val partidas: StateFlow<Map<Int, Int>> = _partidas.asStateFlow()

    fun sumarPartida(juegoId: Int) {
        _partidas.update { it + (juegoId to (it[juegoId] ?: 0) + 1) }
    }

    fun reiniciarPartidas() {
        _partidas.value = emptyMap()
    }

    fun anadirJuego(titulo: String, genero: String, anio: Int, nota: Double) {
        _juegos.update { juegos ->
            val nuevo = Juego(
                id = (juegos.maxOfOrNull { it.id } ?: 0) + 1,
                titulo = titulo,
                genero = genero,
                anio = anio,
                nota = nota,
                portada = R.drawable.img_portada_generica,
                esNovedad = true
            )
            listOf(nuevo) + juegos
        }
    }
}
