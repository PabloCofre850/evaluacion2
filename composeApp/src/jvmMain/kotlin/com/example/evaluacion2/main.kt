package com.example.evaluacion2

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "evaluacion2",
    ) {
        App()
    }
}