package com.example.evaluacion2

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp
import androidx.compose.desktop.ui.tooling.preview.Preview

@Composable
@Preview
fun PantallaMain() {
    var abrirClientes by remember { mutableStateOf(false) }
    var abrirMedidores by remember { mutableStateOf(false) }
    var abrirLecturas by remember { mutableStateOf(false) }
    var abrirBoletas by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Sección izquierda (texto)
        Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("¡Bienvenido!", style = MaterialTheme.typography.headlineMedium)
        }

        Spacer(Modifier.width(40.dp))

        // Sección derecha (botones)
        Column(
            modifier = Modifier.weight(1f).fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { abrirClientes = true },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(220.dp).height(50.dp)
            ) { Text("Gestionar Clientes") }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { abrirMedidores = true },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(220.dp).height(50.dp)
            ) { Text("Gestionar Medidores") }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { abrirLecturas = true },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(220.dp).height(50.dp)
            ) { Text("Gestionar Lecturas") }

            Spacer(Modifier.height(16.dp))
            Button(
                onClick = { abrirBoletas = true },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.width(220.dp).height(50.dp)
            ) { Text("Gestionar Boletas") }
        }
    }

    // Ventanas separadas
    if (abrirClientes) {
        Window(
            onCloseRequest = { abrirClientes = false },
            title = "Gestión de Clientes",
            state = rememberWindowState(width = 600.dp, height = 400.dp)
        ) { PantallaClientes() }
    }

    if (abrirMedidores) {
        Window(
            onCloseRequest = { abrirMedidores = false },
            title = "Gestión de Medidores",
            state = rememberWindowState(width = 600.dp, height = 400.dp)
        ) { PantallaMedidores() }
    }

    if (abrirLecturas) {
        Window(
            onCloseRequest = { abrirLecturas = false },
            title = "Gestión de Lecturas",
            state = rememberWindowState(width = 600.dp, height = 400.dp)
        ) { PantallaLecturas() }
    }

    if (abrirBoletas) {
        Window(
            onCloseRequest = { abrirBoletas = false },
            title = "Gestión de Boletas",
            state = rememberWindowState(width = 600.dp, height = 400.dp)
        ) { PantallaBoletas() }
    }
}

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Pantalla Principal",
        state = rememberWindowState(width = 900.dp, height = 500.dp)
    ) {
        PantallaMain()
    }
}

// Ejemplos simples de pantallas
@Composable fun PantallaClientes() { Text("Pantalla de Clientes") }
@Composable fun PantallaMedidores() { Text("Pantalla de Medidores") }
@Composable fun PantallaLecturas() { Text("Pantalla de Lecturas") }
@Composable fun PantallaBoletas() { Text("Pantalla de Boletas") }