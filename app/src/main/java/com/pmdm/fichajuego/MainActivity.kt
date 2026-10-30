package com.pmdm.fichajuego

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pmdm.fichajuego.ui.biblioteca.BibliotecaScreen
import com.pmdm.fichajuego.ui.theme.FichaJuegoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FichaJuegoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    BibliotecaScreen(
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp)
                    )
                }
            }
        }
    }
}
