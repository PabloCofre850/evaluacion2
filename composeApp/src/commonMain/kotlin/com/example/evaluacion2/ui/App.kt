package com.example.evaluacion2.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
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


// Definimos colores para la aplicacion
private val CGEBlue      = Color(0xFF4A148C)
private val CGELightBlue = Color(0xFF7B1FA2)
private val CGEGray      = Color(0xFFF3E5F5)

private val CGEColorScheme = lightColorScheme(
    primary            = CGEBlue,
    onPrimary          = Color.White,
    primaryContainer   = CGELightBlue,
    onPrimaryContainer = Color.White,
    secondary          = CGELightBlue,
    onSecondary        = Color.White,
    background         = CGEGray,
    onBackground       = Color.Black,
    surface            = Color.White,
    onSurface          = Color.Black,
)

// Definimos una tipografia para utilizar en la aplicacion
private val CGETypography = Typography(
    displayLarge  = TextStyle(fontSize = 32.sp, fontWeight = FontWeight.Bold),
    titleMedium   = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.SemiBold),
    bodyLarge     = TextStyle(fontSize = 16.sp))

@Composable

fun App() {

    val nav = remember { NavigationController() } // var clase navegacion entre pantallas

    val storageDriver = remember { StorageDriver() }

    val medidorRepo = remember { MedidorRepoImpl(PersistenciaDatos(storageDriver)) }

    MaterialTheme(
        colorScheme = CGEColorScheme,
        typography  = CGETypography,
        content     = {
            when (nav.currentScreen) {
                Screen.Menu     -> PantallaMenu(
                    onClientes = { nav.goTo(Screen.Clientes) },
                    onMedidores = { nav.goTo(Screen.Medidores) },
                    onLecturas = { nav.goTo(Screen.Lecturas) },
                    onBoletas  = { nav.goTo(Screen.Boletas) }
                )
                Screen.Clientes -> PantallaClientes(onVolver = nav::backToMenu)

                Screen.Medidores -> PantallaMedidores(
                    repo     = medidorRepo,
                    onVolver = nav::backToMenu
                )

                Screen.Lecturas -> PantallaLecturas(onVolver = nav::backToMenu)

                Screen.Boletas  -> PantallaBoletas(onVolver = nav::backToMenu)
            }
        }
    )

}
