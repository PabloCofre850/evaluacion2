package com.example.evaluacion2

import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import com.example.evaluacion2.ui.App

fun main() = application {

    val state = rememberWindowState(
        width = 1920.dp,
        height = 1080.dp,
        position = WindowPosition.Aligned(Alignment.Center)
    )

    Window(
        onCloseRequest = ::exitApplication,
        title = "Pantalla Principal",
        state = state,
    ) {
        App()
    }
}