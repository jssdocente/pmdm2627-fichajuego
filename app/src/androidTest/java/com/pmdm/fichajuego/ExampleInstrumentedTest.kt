package com.pmdm.fichajuego

import androidx.compose.ui.test.junit4.v2.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test instrumentado de ejemplo: se ejecuta en un emulador o dispositivo.
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun fichaMuestraElTitulo() {
        composeTestRule.onNodeWithText("Hollow Knight").assertExists()
    }
}
