package com.pmdm.fichajuego

import android.app.Application
import com.pmdm.fichajuego.data.JuegoRepositorio

/**
 * Se crea al arrancar la app, antes que cualquier Activity, y vive mientras viva la app.
 * Por eso es el sitio para crear UNA sola vez lo que comparten todas las pantallas.
 * Se registra en el AndroidManifest con android:name=".FichaJuegoApplication".
 */
class FichaJuegoApplication : Application() {
    val repositorio = JuegoRepositorio()
}
