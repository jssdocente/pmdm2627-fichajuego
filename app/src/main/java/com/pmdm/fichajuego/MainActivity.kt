package com.pmdm.fichajuego

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pmdm.fichajuego.ui.biblioteca.BibliotecaScreen
import com.pmdm.fichajuego.ui.theme.FichaJuegoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FichaJuegoTheme {
                // El Scaffold vive ahora dentro de BibliotecaScreen: cada pantalla tiene su barra
                BibliotecaScreen()
            }
        }
    }
}
