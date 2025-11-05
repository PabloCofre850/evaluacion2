package com.example.evaluacion2.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

enum class Screen {
    Menu,
    Clientes,
    Medidores,
    Lecturas,
    Boletas
}

class NavigationController {
    var currentScreen: Screen by mutableStateOf(Screen.Menu)  // Var de pantalla actual (observable)
        private set

    fun goTo(screen: Screen) { // fun para cambiar pantalla
        currentScreen = screen
    }

    fun backToMenu() { // fun volver al menu
        currentScreen = Screen.Menu
    }
}
