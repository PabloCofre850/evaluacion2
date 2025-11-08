package com.example.evaluacion2.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.evaluacion2.shared.persistencia.MedidorRepoImpl
import com.example.evaluacion2.shared.persistencia.PersistenciaDatos
import com.example.evaluacion2.shared.persistencia.StorageDriver
import com.example.evaluacion2.ui.navigation.NavigationController
import com.example.evaluacion2.ui.navigation.Screen
import com.example.evaluacion2.ui.screens.PantallaBoletas
import com.example.evaluacion2.ui.screens.PantallaClientes
import com.example.evaluacion2.ui.screens.PantallaLecturas
import com.example.evaluacion2.ui.screens.PantallaMedidores
import com.example.evaluacion2.ui.screens.PantallaMenu

@Composable
fun App() {
    val nav = remember { NavigationController() } // var clase navegación entre pantallas

    val storageDriver = remember { StorageDriver() }

    val medidorRepo = remember { MedidorRepoImpl(PersistenciaDatos(storageDriver)) }

    when (nav.currentScreen) { // When para cambiar entre pantallas
        Screen.Menu -> PantallaMenu(
            onClientes = { nav.goTo(Screen.Clientes) },
            onMedidores = { nav.goTo(Screen.Medidores) },
            onLecturas = { nav.goTo(Screen.Lecturas) },
            onBoletas = { nav.goTo(Screen.Boletas) }
        )
        Screen.Clientes  -> PantallaClientes(onVolver = nav::backToMenu)

        Screen.Medidores -> PantallaMedidores(
            repo = medidorRepo,
            onVolver = nav::backToMenu
        )

        Screen.Lecturas  -> PantallaLecturas(onVolver = nav::backToMenu)

        Screen.Boletas   -> PantallaBoletas(onVolver = nav::backToMenu)
    }
}
