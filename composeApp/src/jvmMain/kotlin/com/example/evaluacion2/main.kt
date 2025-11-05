package com.example.evaluacion2

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp
import com.example.evaluacion2.ui.screens.PantallaMenu

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Pantalla Principal",
        state = rememberWindowState(width = 900.dp, height = 500.dp)
    ) {
        PantallaMenu()
    }
}